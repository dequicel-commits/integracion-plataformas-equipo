package cl.iplacex.peluditos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.external.client.AcquiredExternalWorkerJob;
import org.flowable.external.client.ExternalWorkerClient;
import org.flowable.external.client.impl.JavaHttpClientRestInvoker;
import org.flowable.external.client.impl.RestExternalWorkerClient;

public class App {

    private static final String TOPIC_GUARDAR_CLIENTE = "guardar-cliente-peluditos";
    private static final String TOPIC_ACTUALIZAR_SEGUIMIENTO = "actualizar-seguimiento-peluditos";

    public static void main(String[] args) throws Exception {

        String urlEnv = System.getenv("FLOWABLE_API_URL");
        String usuarioEnv = System.getenv("FLOWABLE_USER");
        String claveEnv = System.getenv("FLOWABLE_PASSWORD");

        List<ExternalWorkerClient> clientesWorker;
        if (urlEnv != null && !urlEnv.isBlank()) {
            String usuario = (usuarioEnv != null && !usuarioEnv.isBlank())
                    ? usuarioEnv
                    : (urlEnv.contains("flowable-rest") ? "rest-admin" : "admin");
            String clave = (claveEnv != null && !claveEnv.isBlank()) ? claveEnv : "test";
            clientesWorker = List.of(RestExternalWorkerClient.create(
                    "peluditos-worker-entrega3",
                    JavaHttpClientRestInvoker.withBasicAuth(urlEnv, usuario, clave)
            ));
        } else {
            // Si no se especifico FLOWABLE_API_URL, revisar tanto flowable-rest como flowable-ui en :8080
            String clave = (claveEnv != null && !claveEnv.isBlank()) ? claveEnv : "test";
            clientesWorker = List.of(
                    RestExternalWorkerClient.create(
                            "peluditos-worker-rest",
                            JavaHttpClientRestInvoker.withBasicAuth(
                                    "http://localhost:8080/flowable-rest/external-job-api", "rest-admin", clave)
                    ),
                    RestExternalWorkerClient.create(
                            "peluditos-worker-ui",
                            JavaHttpClientRestInvoker.withBasicAuth(
                                    "http://localhost:8080/flowable-ui/external-job-api", "admin", clave)
                    )
            );
        }

        ObjectMapper mapper = new ObjectMapper();
        HttpClient clienteHttp = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        boolean modoContinuo = Arrays.asList(args).contains("--loop")
                || "true".equalsIgnoreCase(System.getenv("WORKER_LOOP"));

        do {
            for (ExternalWorkerClient worker : clientesWorker) {
                try {
                    // 1. Ejecutar Worker 1: Guardar datos del potencial cliente y su mascota
                    procesarGuardarClientes(worker, mapper, clienteHttp, !modoContinuo);

                    // 2. Ejecutar Worker 2: Actualizar resultado del seguimiento comercial (con o sin compra)
                    procesarActualizarSeguimiento(worker, mapper, clienteHttp, !modoContinuo);
                } catch (Exception ignored) {
                    // Si uno de los dos entornos (flowable-rest o flowable-ui) no esta activo, continuar con el otro
                }
            }

            if (modoContinuo) {
                Thread.sleep(2000);
            }
        } while (modoContinuo);
    }

    /**
     * WORKER 1 (topic: guardar-cliente-peluditos)
     * Toma los datos ingresados al inicio del proceso BPMN, invoca POST /api/clientes
     * en el servicio Jakarta REST (:7072) y devuelve a Flowable los IDs generados en SQLite.
     */
    private static void procesarGuardarClientes(
            ExternalWorkerClient worker,
            ObjectMapper mapper,
            HttpClient clienteHttp,
            boolean mostrarSiCero
    ) {
        List<AcquiredExternalWorkerJob> trabajos =
                worker.createJobAcquireBuilder()
                        .topic(TOPIC_GUARDAR_CLIENTE)
                        .numberOfTasks(5)
                        .lockDuration(Duration.ofMinutes(2))
                        .acquireAndLock();

        if (mostrarSiCero || !trabajos.isEmpty()) {
            System.out.println("[Worker 1] Trabajos recibidos (topic: " + TOPIC_GUARDAR_CLIENTE + "): " + trabajos.size());
        }

        for (AcquiredExternalWorkerJob trabajo : trabajos) {
            try {
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
                System.out.println("[Worker 1] Enviando POST /api/clientes: " + jsonBody);

                HttpResponse<String> respuesta = enviarPeticionServicio(clienteHttp, "POST", "/api/clientes", jsonBody);
                System.out.println("[Worker 1] Respuesta de /api/clientes (HTTP " + respuesta.statusCode() + "): " + respuesta.body());

                if (respuesta.statusCode() == 200 || respuesta.statusCode() == 201) {
                    JsonNode nodo = mapper.readTree(respuesta.body());
                    int clienteId = nodo.path("clienteId").asInt(1);
                    int mascotaId = nodo.path("mascotaId").asInt(1);
                    boolean compraRealizadaInicial = leerBooleano(vars, "compraRealizada", true);

                    worker.createCompletionBuilder(trabajo)
                            .variable("clienteRegistrado", true)
                            .variable("clienteId", clienteId)
                            .variable("mascotaId", mascotaId)
                            .variable("compraRealizada", compraRealizadaInicial)
                            .complete();

                    System.out.println("[Worker 1] Trabajo completado en Flowable (jobId: " + trabajo.getId()
                            + ", clienteId: " + clienteId + ", mascotaId: " + mascotaId + ").");
                } else {
                    reportarFalloFlowable(worker, trabajo,
                            "Error en POST /api/clientes: HTTP " + respuesta.statusCode(),
                            respuesta.body());
                }
            } catch (Exception e) {
                reportarFalloFlowable(worker, trabajo,
                        "Excepcion al ejecutar Worker 1 (guardar-cliente-peluditos): " + e.getMessage(),
                        e.toString());
            }
        }
    }

    /**
     * WORKER 2 (topic: actualizar-seguimiento-peluditos)
     * Se ejecuta al cerrar el seguimiento comercial (ruta SI con compra o ruta NO sin compra).
     * Toma clienteId, compraRealizada y observaciones desde Flowable e invoca
     * PUT /api/clientes/{clienteId}/seguimiento en el servicio Jakarta REST (:7072).
     */
    private static void procesarActualizarSeguimiento(
            ExternalWorkerClient worker,
            ObjectMapper mapper,
            HttpClient clienteHttp,
            boolean mostrarSiCero
    ) {
        List<AcquiredExternalWorkerJob> trabajos =
                worker.createJobAcquireBuilder()
                        .topic(TOPIC_ACTUALIZAR_SEGUIMIENTO)
                        .numberOfTasks(5)
                        .lockDuration(Duration.ofMinutes(2))
                        .acquireAndLock();

        if (mostrarSiCero || !trabajos.isEmpty()) {
            System.out.println("[Worker 2] Trabajos recibidos (topic: " + TOPIC_ACTUALIZAR_SEGUIMIENTO + "): " + trabajos.size());
        }

        for (AcquiredExternalWorkerJob trabajo : trabajos) {
            try {
                Map<String, Object> vars = trabajo.getVariables();
                String elementId = trabajo.getElementId() != null ? trabajo.getElementId() : "";

                // Determinar si la compra fue realizada segun la variable de Flowable o la actividad del BPMN
                boolean porDefectoCompro = !elementId.equalsIgnoreCase("FormTask_11");
                boolean compraRealizada = leerBooleano(vars, "compraRealizada", porDefectoCompro);
                if (elementId.equalsIgnoreCase("FormTask_11")) {
                    compraRealizada = false;
                } else if (elementId.equalsIgnoreCase("FormTask_13")) {
                    compraRealizada = true;
                }

                int clienteId = leerEntero(vars, "clienteId", 0);
                if (clienteId <= 0) {
                    clienteId = obtenerUltimoClienteId(clienteHttp, mapper);
                }

                String estadoPorDefecto = compraRealizada ? "Compra realizada" : "Sin compra";
                String estadoCompra = leerTexto(vars, "resultadoSeguimiento", estadoPorDefecto);
                String observaciones = leerTexto(
                        vars,
                        "observacionesSeguimiento",
                        leerTexto(vars, "clienteObservaciones",
                                compraRealizada
                                        ? "Seguimiento finalizado: venta concretada en Flowable"
                                        : "Seguimiento finalizado: potencial cliente no concreto la compra")
                );

                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("compraRealizada", compraRealizada);
                payload.put("clienteEstadoCompra", estadoCompra);
                payload.put("clienteObservaciones", observaciones);

                String jsonBody = mapper.writeValueAsString(payload);
                String ruta = "/api/clientes/" + clienteId + "/seguimiento";
                System.out.println("[Worker 2] Enviando PUT " + ruta + ": " + jsonBody);

                HttpResponse<String> respuesta = enviarPeticionServicio(clienteHttp, "PUT", ruta, jsonBody);
                System.out.println("[Worker 2] Respuesta de " + ruta + " (HTTP " + respuesta.statusCode() + "): " + respuesta.body());

                if (respuesta.statusCode() == 200 || respuesta.statusCode() == 201) {
                    worker.createCompletionBuilder(trabajo)
                            .variable("seguimientoActualizado", true)
                            .variable("clienteEstadoCompra", estadoCompra)
                            .variable("compraRealizada", compraRealizada)
                            .complete();

                    System.out.println("[Worker 2] Trabajo completado en Flowable (jobId: " + trabajo.getId()
                            + ", clienteId: " + clienteId + ", estado: " + estadoCompra + ").");
                } else {
                    reportarFalloFlowable(worker, trabajo,
                            "Error en PUT " + ruta + ": HTTP " + respuesta.statusCode(),
                            respuesta.body());
                }
            } catch (Exception e) {
                reportarFalloFlowable(worker, trabajo,
                        "Excepcion al ejecutar Worker 2 (actualizar-seguimiento-peluditos): " + e.getMessage(),
                        e.toString());
            }
        }
    }

    private static void reportarFalloFlowable(
            ExternalWorkerClient worker,
            AcquiredExternalWorkerJob trabajo,
            String mensajeError,
            String detalleError
    ) {
        System.err.println("[Error Worker] " + mensajeError + " (jobId: " + trabajo.getId() + ")");
        try {
            worker.createFailureBuilder(trabajo)
                    .message(mensajeError)
                    .details(detalleError)
                    .retries(1)
                    .retryTimeout(Duration.ofSeconds(15))
                    .fail();
        } catch (Exception ex) {
            System.err.println("[Error Worker] No se pudo notificar el fallo a Flowable: " + ex.getMessage());
        }
    }

    private static int obtenerUltimoClienteId(HttpClient cliente, ObjectMapper mapper) {
        try {
            HttpResponse<String> resp = enviarPeticionServicio(cliente, "GET", "/api/clientes", null);
            if (resp.statusCode() == 200) {
                JsonNode lista = mapper.readTree(resp.body());
                if (lista.isArray() && !lista.isEmpty()) {
                    return lista.get(0).path("CLIENTE_ID").asInt(1);
                }
            }
        } catch (Exception ignored) {
        }
        return 1;
    }

    private static int inferirTipoMascota(String consulta, String observaciones) {
        String texto = (consulta + " " + observaciones).toUpperCase();
        if (texto.contains("GAT")) {
            return 4; // Gato Mediano (precargado en TABLA_TIPO_MASCOTA con ID 4)
        }
        if (texto.contains("BEBE") || texto.contains("PEQUE") || texto.contains("CACHORR")) {
            return 1; // Perro Pequeno
        }
        if (texto.contains("GRANDE")) {
            return 3; // Perro Grande
        }
        return 2; // Perro Mediano
    }

    private static HttpResponse<String> enviarPeticionServicio(
            HttpClient cliente,
            String metodo,
            String ruta,
            String jsonBody
    ) throws Exception {
        String baseConfigurada = System.getenv().getOrDefault("SERVICIO_PELUDITOS_URL", "http://localhost:7072");
        // Si SERVICIO_PELUDITOS_URL incluia /api/clientes, extraer solo el host:puerto
        int idxApi = baseConfigurada.indexOf("/api/");
        String basePrincipal = idxApi > 0 ? baseConfigurada.substring(0, idxApi) : baseConfigurada;

        String[] basesCandidatas = new String[] {
                basePrincipal,
                "http://localhost:7072",
                "http://localhost:7070",
                "http://localhost:7071"
        };

        Exception ultimoError = null;
        for (String base : basesCandidatas) {
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(base + ruta))
                        .timeout(Duration.ofSeconds(5));

                if ("POST".equalsIgnoreCase(metodo)) {
                    builder.header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : "{}"));
                } else if ("PUT".equalsIgnoreCase(metodo)) {
                    builder.header("Content-Type", "application/json")
                            .PUT(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : "{}"));
                } else {
                    builder.GET();
                }

                HttpResponse<String> resp = cliente.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    return resp;
                }
            } catch (Exception e) {
                ultimoError = e;
            }
        }

        if (ultimoError != null) {
            throw ultimoError;
        }
        throw new IllegalStateException("No se pudo conectar con servicio-peluditos en 7072, 7070 ni 7071.");
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
