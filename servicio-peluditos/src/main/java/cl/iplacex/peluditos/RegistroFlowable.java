package cl.iplacex.peluditos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

final class RegistroFlowable {

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

    synchronized void registrar(Context ctx) throws IOException {
        if (!InetAddress.getByName(ctx.req().getRemoteAddr()).isLoopbackAddress()) {
            ctx.status(403).json(Map.of("mensaje", "El formulario se ejecuta en el equipo local."));
            return;
        }

        String tipo = ctx.req().getContentType();
        if (tipo == null || !tipo.toLowerCase(Locale.ROOT).startsWith("application/json")) {
            ctx.status(415).json(Map.of("mensaje", "El registro debe enviarse en formato JSON."));
            return;
        }

        String referencia;
        try {
            referencia = "web-" + UUID.fromString(ctx.pathParam("referencia"));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("mensaje", "La referencia del registro no es válida."));
            return;
        }

        JsonNode datos = ctx.bodyAsClass(JsonNode.class);
        List<Map<String, Object>> variables = new ArrayList<>();
        for (String campo : OBLIGATORIOS) {
            String valor = datos.path(campo).asText("").trim();
            if (valor.isEmpty()) {
                ctx.status(400).json(Map.of("mensaje", "Completa los campos obligatorios."));
                return;
            }
            variables.add(Map.of("name", campo, "value", valor));
        }
        for (String campo : OPCIONALES) {
            String valor = datos.path(campo).asText("").trim();
            if (!valor.isEmpty()) {
                variables.add(Map.of("name", campo, "value", valor));
            }
        }
        variables.add(Map.of("name", "clienteAutoriza", "value", datos.path("clienteAutoriza").asBoolean()));

        if (usuario == null || clave == null) {
            ctx.status(503).json(Map.of("mensaje", "Configura el usuario y la clave de Flowable en el servicio."));
            return;
        }

        try {
            String filtro = "?processDefinitionKey=" + PROCESO + "&businessKey=" + referencia;
            JsonNode casos = enviar("/history/historic-process-instances" + filtro, null).path("data");
            JsonNode caso = casos.isEmpty()
                ? enviar("/runtime/process-instances", Map.of("processDefinitionKey", PROCESO, "businessKey", referencia))
                : casos.get(0);
            String casoId = caso.path("id").asText();
            String tareas = "?processInstanceId=" + casoId + "&taskDefinitionKey=" + TAREA;
            JsonNode pendientes = enviar("/runtime/tasks" + tareas, null).path("data");

            if (!pendientes.isEmpty()) {
                String tareaId = pendientes.get(0).path("id").asText();
                enviar("/runtime/tasks/" + tareaId, Map.of("action", "complete", "variables", variables));
            } else {
                JsonNode completadas = enviar("/history/historic-task-instances" + tareas + "&finished=true", null)
                    .path("data");
                if (completadas.isEmpty()) {
                    ctx.status(409).json(Map.of("mensaje", "No se encontró la tarea de registro en Flowable."));
                    return;
                }
            }
            ctx.status(202).json(Map.of("casoId", casoId));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ctx.status(503).json(Map.of("mensaje", "Se interrumpió la conexión con Flowable. Intenta de nuevo."));
        } catch (IOException e) {
            System.err.println("Error en el registro Flowable: " + e.getMessage());
            ctx.status(502).json(Map.of("mensaje", "No se pudo confirmar el registro en Flowable. Intenta de nuevo."));
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
