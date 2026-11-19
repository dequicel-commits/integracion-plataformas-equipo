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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.ws.rs.core.Response;

final class RegistroFlowableJakarta {

    private static final String PROCESO = "peluditosPetChileTOBE12";
    private static final String TAREA = "FormTask_1";
    private static final List<String> OBLIGATORIOS = List.of(
        "clienteNombre", "clienteTelefono", "clienteCanalContacto",
        "clienteConsultaRealizada", "mascotaNombre"
    );
    private static final List<String> OPCIONALES = List.of(
        "clienteCorreo", "mascotaCumpleano", "mascotaObservaciones"
    );

    private final ObjectMapper json = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5)).build();
    private final String url = System.getenv().getOrDefault(
        "FLOWABLE_REST_URL", "http://127.0.0.1:8080/flowable-rest/service"
    );
    private final String usuario = System.getenv("FLOWABLE_USER");
    private final String clave = System.getenv("FLOWABLE_PASSWORD");

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
            "value", datos.path("clienteAutoriza").asBoolean()
    ));

    if (usuario == null || clave == null) {
        return Response.status(503)
                .entity(Map.of(
                        "mensaje", "Configura el usuario y la clave de Flowable en el servicio."
                ))
                .build();
    }

    try {
        String filtro = "?processDefinitionKey=" + PROCESO
                + "&businessKey=" + referenciaFlowable;

        JsonNode casos = enviar(
                "/history/historic-process-instances" + filtro, null
        ).path("data");

        JsonNode caso = casos.isEmpty()
                ? enviar("/runtime/process-instances",
                        Map.of(
                                "processDefinitionKey", PROCESO,
                                "businessKey", referenciaFlowable
                        ))
                : casos.get(0);

        String casoId = caso.path("id").asText();

        String tareas = "?processInstanceId=" + casoId
                + "&taskDefinitionKey=" + TAREA;

        JsonNode pendientes = enviar(
                "/runtime/tasks" + tareas, null
        ).path("data");

        if (!pendientes.isEmpty()) {
            String tareaId = pendientes.get(0).path("id").asText();

            enviar("/runtime/tasks/" + tareaId,
                    Map.of(
                            "action", "complete",
                            "variables", variables
                    ));
        } else {
            JsonNode completadas = enviar(
                    "/history/historic-task-instances"
                            + tareas + "&finished=true", null
            ).path("data");

            if (completadas.isEmpty()) {
                return Response.status(409)
                        .entity(Map.of(
                                "mensaje", "No se encontró la tarea de registro en Flowable."
                        ))
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
                        "mensaje", "Se interrumpió la conexión con Flowable. Intenta de nuevo."
                ))
                .build();

    } catch (IOException e) {
        System.err.println("Error en el registro Flowable: " + e.getMessage());

        return Response.status(502)
                .entity(Map.of(
                        "mensaje", "No se pudo confirmar el registro en Flowable. Intenta de nuevo."
                ))
                .build();
    }
}
    private JsonNode enviar(String ruta, Map<String, ?> cuerpo) throws IOException, InterruptedException {
        String token = Base64.getEncoder().encodeToString((usuario + ":" + clave).getBytes(StandardCharsets.UTF_8));
        HttpRequest.Builder solicitud = HttpRequest.newBuilder(URI.create(url + ruta))
            .timeout(Duration.ofSeconds(10)).header("Authorization", "Basic " + token);

        if (cuerpo == null) {
            solicitud.GET();
        } else {
            solicitud.header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(cuerpo)));
        }
        HttpResponse<String> respuesta = http.send(solicitud.build(), HttpResponse.BodyHandlers.ofString());
        if (respuesta.statusCode() < 200 || respuesta.statusCode() >= 300) {
            throw new IOException(ruta + " respondió HTTP " + respuesta.statusCode());
        }
        return respuesta.body().isBlank() ? json.nullNode() : json.readTree(respuesta.body());
    }
}
