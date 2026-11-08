package cl.iplacex.peluditos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.external.client.AcquiredExternalWorkerJob;
import org.flowable.external.client.ExternalWorkerClient;
import org.flowable.external.client.impl.JavaHttpClientRestInvoker;
import org.flowable.external.client.impl.RestExternalWorkerClient;

public class App {

    public static void main(String[] args) throws Exception {

        String url = System.getenv("FLOWABLE_API_URL");
        String usuario = System.getenv("FLOWABLE_USER");
        String clave = System.getenv("FLOWABLE_PASSWORD");

        // Valores por defecto para facilitar pruebas locales si no se definieron variables de entorno
        if (url == null || url.isBlank()) {
            url = "http://localhost:8080/flowable-ui/external-job-api";
        }
        if (usuario == null || usuario.isBlank()) {
            usuario = url.contains("flowable-rest") ? "rest-admin" : "admin";
        }
        if (clave == null || clave.isBlank()) {
            clave = "test";
        }

        ExternalWorkerClient worker = RestExternalWorkerClient.create(
                "peluditos-worker-entrega2",
                JavaHttpClientRestInvoker.withBasicAuth(url, usuario, clave)
        );

        List<AcquiredExternalWorkerJob> trabajos =
                worker.createJobAcquireBuilder()
                        .topic("guardar-cliente-peluditos")
                        .numberOfTasks(5)
                        .lockDuration(Duration.ofMinutes(2))
                        .acquireAndLock();

        System.out.println("Trabajos recibidos (topic: guardar-cliente-peluditos): " + trabajos.size());

        ObjectMapper mapper = new ObjectMapper();
        HttpClient clienteHttp = HttpClient.newHttpClient();

        for (AcquiredExternalWorkerJob trabajo : trabajos) {
            Map<String, Object> vars = trabajo.getVariables();

            String consulta = leerTexto(vars, "clienteConsultaRealizada", "Torta de cumpleanos para perro mediano");
            String obsMascota = leerTexto(vars, "mascotaObservaciones", "Sin alergias alimentarias");
            int tipoMascotaPorDefecto = inferirTipoMascota(consulta, obsMascota);

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("clienteNombre", leerTexto(vars, "clienteNombre", "Camila Rojas"));
            payload.put("clienteTelefono", leerTexto(vars, "clienteTelefono", "+56987654321"));
            payload.put("clienteCorreo", leerTexto(vars, "clienteCorreo", "camila.rojas@correo.cl"));
            payload.put("clienteCanalContacto", leerTexto(vars, "clienteCanalContacto", "Instagram"));
            payload.put("clienteFechaConsulta", leerTexto(vars, "clienteFechaConsulta", LocalDate.now().toString()));
            payload.put("clienteConsultaRealizada", consulta);
            payload.put("clienteEstadoCompra", leerTexto(vars, "clienteEstadoCompra", "En seguimiento"));
            payload.put("clienteAutoriza", leerBooleano(vars, "clienteAutoriza", true));
            payload.put("clienteObservaciones", leerTexto(vars, "clienteObservaciones", "Registrado desde Flowable"));
            payload.put("mascotaNombre", leerTexto(vars, "mascotaNombre", "Toby"));
            payload.put("mascotaTipoId", leerEntero(vars, "mascotaTipoId", tipoMascotaPorDefecto));
            payload.put("mascotaCumpleano", leerTexto(vars, "mascotaCumpleano", "2026-11-15"));
            payload.put("mascotaObservaciones", obsMascota);

            String jsonBody = mapper.writeValueAsString(payload);
            System.out.println("Enviando datos a /api/clientes: " + jsonBody);

            HttpResponse<String> respuesta = enviarPostCliente(clienteHttp, jsonBody);
            System.out.println("Respuesta del servicio REST (/api/clientes): " + respuesta.body());

            if (respuesta.statusCode() == 200 || respuesta.statusCode() == 201) {
                worker.createCompletionBuilder(trabajo)
                        .variable("clienteRegistrado", true)
                        .variable("compraRealizada", true)
                        .complete();
                System.out.println("Trabajo completado en Flowable (jobId: " + trabajo.getId() + ").");
            } else {
                System.out.println("El servicio no respondio correctamente (HTTP " + respuesta.statusCode() + ").");
            }
        }
    }

    private static int inferirTipoMascota(String consulta, String observaciones) {
        String texto = (consulta + " " + observaciones).toUpperCase();
        if (texto.contains("GAT")) {
            if (texto.contains("BEBE") || texto.contains("PEQUE") || texto.contains("CACHORR")) {
                return 4; // Gato Pequeno
            }
            if (texto.contains("GRANDE")) {
                return 6; // Gato Grande
            }
            return 5; // Gato Mediano
        }
        if (texto.contains("BEBE") || texto.contains("PEQUE") || texto.contains("CACHORR")) {
            return 1; // Perro Pequeno
        }
        if (texto.contains("GRANDE")) {
            return 3; // Perro Grande
        }
        return 2; // Perro Mediano
    }

    private static HttpResponse<String> enviarPostCliente(HttpClient cliente, String jsonBody) throws Exception {
        String servicioUrl = System.getenv().getOrDefault("SERVICIO_PELUDITOS_URL", "http://localhost:7071/api/clientes");
        try {
            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create(servicioUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> resp = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200 || resp.statusCode() == 201) {
                return resp;
            }
        } catch (Exception ignored) {
            // Intentar puerto alternativo (7070) si 7071 no esta activo
        }

        HttpRequest solicitudAlt = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7070/api/clientes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return cliente.send(solicitudAlt, HttpResponse.BodyHandlers.ofString());
    }

    private static String leerTexto(Map<String, Object> vars, String clave, String porDefecto) {
        if (vars != null && vars.containsKey(clave) && vars.get(clave) != null) {
            String valor = String.valueOf(vars.get(clave)).trim();
            if (!valor.isEmpty()) {
                return valor;
            }
        }
        return porDefecto;
    }

    private static boolean leerBooleano(Map<String, Object> vars, String clave, boolean porDefecto) {
        if (vars != null && vars.containsKey(clave) && vars.get(clave) != null) {
            Object val = vars.get(clave);
            if (val instanceof Boolean b) {
                return b;
            }
            return Boolean.parseBoolean(String.valueOf(val));
        }
        return porDefecto;
    }

    private static int leerEntero(Map<String, Object> vars, String clave, int porDefecto) {
        if (vars != null && vars.containsKey(clave) && vars.get(clave) != null) {
            Object val = vars.get(clave);
            if (val instanceof Number n) {
                return n.intValue();
            }
            try {
                return Integer.parseInt(String.valueOf(val).trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return porDefecto;
    }
}
