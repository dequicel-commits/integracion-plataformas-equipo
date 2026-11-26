package cl.iplacex.peluditos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.ws.rs.core.Response;

final class RegistroFlowableJakarta {

    private static final String PROCESO = "peluditosPetChileTOBE12";
    private static final String TAREA = "FormTask_1";
    private static final String TAREA_ATENCION = "FormTask_3";
    private static final String TAREA_SEGUIMIENTO = "FormTask_5";

    private static final List<String> OBLIGATORIOS = List.of(
            "clienteNombre", "clienteTelefono", "clienteCanalContacto",
            "clienteConsultaRealizada", "mascotaNombre");
    private static final List<String> OPCIONALES = List.of(
            "clienteCorreo", "mascotaCumpleano", "mascotaObservaciones");

    private final ObjectMapper json = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final String url = System.getenv().getOrDefault(
            "FLOWABLE_REST_URL", "http://127.0.0.1:8080/flowable-rest/service");
    private final String usuario = obtenerUsuarioPorDefecto();
    private final String clave = obtenerClavePorDefecto();

    private String obtenerUsuarioPorDefecto() {
        String envUser = System.getenv("FLOWABLE_USER");
        if (envUser != null && !envUser.isBlank()) {
            return envUser;
        }
        return url.contains("flowable-rest") ? "rest-admin" : "admin";
    }

    private String obtenerClavePorDefecto() {
        String envPass = System.getenv("FLOWABLE_PASSWORD");
        if (envPass != null && !envPass.isBlank()) {
            return envPass;
        }
        return null;
    }

    synchronized Response registrar(String referencia, JsonNode datos) {

        String referenciaFlowable;

        try {
            referenciaFlowable = "web-" + UUID.fromString(referencia);
        } catch (IllegalArgumentException e) {
            return Response.status(400)
                    .entity(Map.of("mensaje", "La referencia del registro no es válida."))
                    .build();
        }

        if (datos == null || !datos.isObject()) {
            return Response.status(400)
                    .entity(Map.of("mensaje", "Los datos del formulario no son válidos."))
                    .build();
        }

        List<Map<String, Object>> variables = new ArrayList<>();

        for (String campo : OBLIGATORIOS) {
            String valor = datos.path(campo).asText("").trim();

            if (valor.isEmpty()) {
                return Response.status(400)
                        .entity(Map.of("mensaje", "Completa los campos obligatorios."))
                        .build();
            }

            variables.add(Map.of("name", campo, "value", valor));
        }

        for (String campo : OPCIONALES) {
            String valor = datos.path(campo).asText("").trim();

            if (!valor.isEmpty()) {
                variables.add(Map.of("name", campo, "value", valor));
            }
        }

        variables.add(Map.of(
                "name", "clienteAutoriza",
                "value", datos.path("clienteAutoriza").asBoolean()));

        try {
            String filtro = "?processDefinitionKey=" + PROCESO
                    + "&businessKey=" + referenciaFlowable;

            JsonNode casos = enviar(
                    "/history/historic-process-instances" + filtro, null).path("data");

            JsonNode caso = casos.isEmpty()
                    ? enviar("/runtime/process-instances",
                            Map.of(
                                    "processDefinitionKey", PROCESO,
                                    "businessKey", referenciaFlowable))
                    : casos.get(0);

            String casoId = caso.path("id").asText();

            String tareas = "?processInstanceId=" + casoId
                    + "&taskDefinitionKey=" + TAREA;

            JsonNode pendientes = enviar(
                    "/runtime/tasks" + tareas, null).path("data");

            if (!pendientes.isEmpty()) {
                String tareaId = pendientes.get(0).path("id").asText();

                enviar("/runtime/tasks/" + tareaId,
                        Map.of(
                                "action", "complete",
                                "variables", variables));
            } else {
                JsonNode completadas = enviar(
                        "/history/historic-task-instances"
                                + tareas + "&finished=true",
                        null).path("data");

                if (completadas.isEmpty()) {
                    return Response.status(409)
                            .entity(Map.of(
                                    "mensaje", "No se encontró la tarea de registro en Flowable."))
                            .build();
                }
            }

            return Response.status(202)
                    .entity(Map.of("casoId", casoId))
                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            return Response.status(503)
                    .entity(Map.of(
                            "mensaje", "Se interrumpió la conexión con Flowable. Intenta de nuevo."))
                    .build();

        } catch (IOException e) {
            System.err.println("Error en el registro Flowable: " + e.getMessage());

            return Response.status(502)
                    .entity(Map.of(
                            "mensaje","No pudimos enviar tu consulta en este momento. Intenta nuevamente más tarde.""))
                    .build();
        }
    }

    /**
     * Avanza en Flowable OSS las tareas de atencion (FormTask_3) y seguimiento
     * comercial (FormTask_5),
     * inyectando compraRealizada (true/false) y observaciones para dejar listo el
     * trabajo del Worker 2.
     */
    synchronized Response avanzarSeguimiento(String casoId, JsonNode datos) {
        boolean compraRealizada = datos.path("compraRealizada").asBoolean(true);
        String observaciones = datos.path("clienteObservaciones").asText(
                datos.path("observacionesSeguimiento").asText("")).trim();
        Integer clienteId = datos.hasNonNull("clienteId") ? datos.path("clienteId").asInt() : null;

        String estadoCompra = compraRealizada ? "Compra realizada" : "Sin compra";
        String obsFinal = !observaciones.isEmpty()
                ? observaciones
                : (compraRealizada
                        ? "Seguimiento finalizado: cliente concreto la compra"
                        : "Seguimiento finalizado: potencial cliente sin compra");

        try {
            List<String> tareasCompletadas = new ArrayList<>();

            // 1. Si FormTask_3 (Atender y responder consulta) esta pendiente, completarla
            // primero
            String filtroAtencion = "?processInstanceId=" + casoId + "&taskDefinitionKey=" + TAREA_ATENCION;
            JsonNode pendientesAtencion = enviar("/runtime/tasks" + filtroAtencion, null).path("data");
            if (!pendientesAtencion.isEmpty()) {
                String tareaAtencionId = pendientesAtencion.get(0).path("id").asText();
                enviar("/runtime/tasks/" + tareaAtencionId, Map.of("action", "complete"));
                tareasCompletadas.add(TAREA_ATENCION);
            }

            // 2. Buscar FormTask_5 (Realizar seguimiento comercial) y completarla
            String filtroSeguimiento = "?processInstanceId=" + casoId + "&taskDefinitionKey=" + TAREA_SEGUIMIENTO;
            JsonNode pendientesSeguimiento = enviar("/runtime/tasks" + filtroSeguimiento, null).path("data");
            if (pendientesSeguimiento.isEmpty()) {
                return Response.status(409)
                        .entity(Map.of(
                                "mensaje",
                                "No hay tarea de seguimiento pendiente para el caso " + casoId
                                        + ". Verifica que el Worker 1 (guardar-cliente-peluditos) ya se haya ejecutado."))
                        .build();
            }

            List<Map<String, Object>> variables = new ArrayList<>();
            variables.add(Map.of("name", "compraRealizada", "value", compraRealizada));
            variables.add(Map.of("name", "resultadoSeguimiento", "value", estadoCompra));
            variables.add(Map.of("name", "observacionesSeguimiento", "value", obsFinal));
            variables.add(Map.of("name", "clienteObservaciones", "value", obsFinal));
            if (clienteId != null && clienteId > 0) {
                variables.add(Map.of("name", "clienteId", "value", clienteId));
            }

            String tareaSeguimientoId = pendientesSeguimiento.get(0).path("id").asText();
            enviar("/runtime/tasks/" + tareaSeguimientoId, Map.of("action", "complete", "variables", variables));
            tareasCompletadas.add(TAREA_SEGUIMIENTO);

            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("estado", "OK");
            respuesta.put("casoId", casoId);
            respuesta.put("compraRealizada", compraRealizada);
            respuesta.put("clienteEstadoCompra", estadoCompra);
            respuesta.put("tareasCompletadas", tareasCompletadas);
            respuesta.put("mensaje",
                    "Seguimiento completado en Flowable. El cierre en BD queda listo para el Worker 2.");

            return Response.ok(respuesta).build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Response.status(503)
                    .entity(Map.of("mensaje", "Se interrumpió la conexión con Flowable."))
                    .build();
        } catch (IOException e) {
            System.err.println("Error al avanzar seguimiento en Flowable: " + e.getMessage());
            return Response.status(502)
                    .entity(Map.of("mensaje", "No se pudo avanzar el seguimiento en Flowable."))
                    .build();
        }
    }

    private JsonNode enviar(String ruta, Map<String, ?> cuerpo) throws IOException, InterruptedException {
        if (clave == null || clave.isBlank()) {
            throw new IOException("Configura FLOWABLE_PASSWORD antes de conectar con Flowable.");
        }

        String[] basesCandidatas = new String[] { url };

        IOException ultimoError = null;
        for (String base : basesCandidatas) {
            try {
                String token = Base64.getEncoder().encodeToString(
                        (usuario + ":" + clave).getBytes(StandardCharsets.UTF_8));

                HttpRequest.Builder solicitud = HttpRequest.newBuilder(URI.create(base + ruta))
                        .version(HttpClient.Version.HTTP_1_1)
                        .timeout(Duration.ofSeconds(10))
                        .header("Authorization", "Basic " + token);

                if (cuerpo == null) {
                    solicitud.GET();
                } else {
                    solicitud.header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(cuerpo)));
                }

                HttpResponse<String> respuesta = http.send(solicitud.build(), HttpResponse.BodyHandlers.ofString());
                if (respuesta.statusCode() >= 200 && respuesta.statusCode() < 300) {
                    
                    return respuesta.body().isBlank() ? json.nullNode() : json.readTree(respuesta.body());
                }
                ultimoError = new IOException(base + ruta + " respondió HTTP " + respuesta.statusCode());
            } catch (IOException e) {
                ultimoError = e;
            }
        }
        throw ultimoError != null ? ultimoError : new IOException("No se pudo conectar con Flowable.");
    }
}
