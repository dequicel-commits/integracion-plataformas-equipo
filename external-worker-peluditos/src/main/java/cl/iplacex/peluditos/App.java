package cl.iplacex.peluditos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private static final String TOPIC_SELECCIONAR_CAMPANA = "seleccionar-clientes-campana";
    private static final String TOPIC_SEGUIMIENTO_CAMPANA = "registrar-seguimiento-campana";


    public static void main(String[] args) throws Exception {

     
        String urlEnv = System.getenv("FLOWABLE_API_URL");
        String usuarioEnv = System.getenv("FLOWABLE_USER");
        String claveEnv = System.getenv("FLOWABLE_PASSWORD");

        if (urlEnv == null || urlEnv.isBlank()) {
            throw new IllegalStateException(
                    "Debes configurar FLOWABLE_API_URL para conectar con Flowable.");
        }

        if (usuarioEnv == null || usuarioEnv.isBlank()) {
            throw new IllegalStateException(
                    "Debes configurar FLOWABLE_USER.");
        }

        if (claveEnv == null || claveEnv.isBlank()) {
            throw new IllegalStateException(
                    "Debes configurar FLOWABLE_PASSWORD.");
        }

        List<ExternalWorkerClient> clientesWorker = List.of(
                RestExternalWorkerClient.create(
                        "peluditos-worker-entrega3",
                        JavaHttpClientRestInvoker.withBasicAuth(
                                urlEnv, usuarioEnv, claveEnv)));


        ObjectMapper mapper = new ObjectMapper();
        HttpClient clienteHttp = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        boolean modoContinuo = Arrays.asList(args).contains("--loop")
                || "true".equalsIgnoreCase(System.getenv("WORKER_LOOP"));

        
        do {
            for (ExternalWorkerClient worker : clientesWorker) {
                try {
                    // 1. Ejecutar Worker 1: Guardar cliente y mascota
                    procesarGuardarClientes(worker, mapper, clienteHttp, !modoContinuo);

                    // 2. Ejecutar Worker 2: Actualizar seguimiento comercial
                    procesarActualizarSeguimiento(worker, mapper, clienteHttp, !modoContinuo);

                    // 3. Seleccionar clientes elegibles para campanas
                    procesarSeleccionarClientesCampana(worker, mapper, clienteHttp, !modoContinuo);

                    // 4. Registrar seguimiento de una campana
                    procesarSeguimientoCampana(worker, mapper, clienteHttp, !modoContinuo);

                } catch (Exception e) {
                    System.err.println(
                            "[Error Worker] No se pudo consultar Flowable: " + e.getMessage());
                }
            }

            if (modoContinuo) {
                Thread.sleep(2000);
            }
        } while (modoContinuo);

    }

    /**
     * WORKER 1 (topic: guardar-cliente-peluditos)
     * Toma los datos ingresados al inicio del proceso BPMN, invoca POST
     * /api/clientes
     * en el servicio Jakarta REST (:7072) y devuelve a Flowable los IDs generados
     * en SQLite.
     */
    private static void procesarGuardarClientes(
            ExternalWorkerClient worker,
            ObjectMapper mapper,
            HttpClient clienteHttp,
            boolean mostrarSiCero) {
        List<AcquiredExternalWorkerJob> trabajos = worker.createJobAcquireBuilder()
                .topic(TOPIC_GUARDAR_CLIENTE)
                .numberOfTasks(5)
                .lockDuration(Duration.ofMinutes(2))
                .acquireAndLock();

        if (mostrarSiCero || !trabajos.isEmpty()) {
            System.out.println(
                    "[Worker 1] Trabajos recibidos (topic: " + TOPIC_GUARDAR_CLIENTE + "): " + trabajos.size());
        }

        for (AcquiredExternalWorkerJob trabajo : trabajos) {
            try {
                Map<String, Object> vars = trabajo.getVariables();
                System.out.println("[QA] Scope ID del proceso: " + trabajo.getScopeId());
                System.out.println("[QA] Scope Type: " + trabajo.getScopeType());

                String consulta = leerTexto(vars, "clienteConsultaRealizada", "Torta de cumpleanos para perro mediano");
                String obsMascota = leerTexto(vars, "mascotaObservaciones", "Sin alergias alimentarias");
                int tipoMascotaPorDefecto = inferirTipoMascota(consulta, obsMascota);

                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("clienteNombre", leerTexto(vars, "clienteNombre", "Camila Rojas"));
                payload.put("clienteTelefono", leerTexto(vars, "clienteTelefono", "+56987654321"));
                payload.put("clienteCorreo", leerTexto(vars, "clienteCorreo", "camila.rojas@correo.cl"));
                payload.put("clienteCanalContacto", leerTexto(vars, "clienteCanalContacto", "Instagram"));
                payload.put("clienteFechaConsulta",leerTexto(vars, "clienteFechaConsulta", LocalDate.now().toString()));
                payload.put("clienteConsultaRealizada", consulta);
                payload.put("clienteEstadoCompra", leerTexto(vars, "clienteEstadoCompra", "En seguimiento"));
                payload.put("clienteAutoriza", leerBooleano(vars, "clienteAutoriza", true));
                payload.put("clienteObservaciones",leerTexto(vars, "clienteObservaciones", "Registrado desde Flowable"));
                payload.put("flowableCasoId", trabajo.getScopeId());
                payload.put("mascotaNombre", leerTexto(vars, "mascotaNombre", "Toby"));
                payload.put("mascotaTipoId", leerEntero(vars, "mascotaTipoId", tipoMascotaPorDefecto));
                payload.put("mascotaCumpleano", leerTexto(vars, "mascotaCumpleano", "2026-11-15"));
                payload.put("mascotaObservaciones", obsMascota);

                String jsonBody = mapper.writeValueAsString(payload);
                System.out.println("[Worker 1] Enviando POST /api/clientes: " + jsonBody);

                HttpResponse<String> respuesta = enviarPeticionServicio(clienteHttp, "POST", "/api/clientes", jsonBody);
                System.out.println("[Worker 1] Respuesta de /api/clientes (HTTP " + respuesta.statusCode() + "): "
                        + respuesta.body());

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
     * Se ejecuta al cerrar el seguimiento comercial (ruta SI con compra o ruta NO
     * sin compra).
     * Toma clienteId, compraRealizada y observaciones desde Flowable e invoca
     * PUT /api/clientes/{clienteId}/seguimiento en el servicio Jakarta REST
     * (:7072).
     */
    private static void procesarActualizarSeguimiento(
            ExternalWorkerClient worker,
            ObjectMapper mapper,
            HttpClient clienteHttp,
            boolean mostrarSiCero) {
        List<AcquiredExternalWorkerJob> trabajos = worker.createJobAcquireBuilder()
                .topic(TOPIC_ACTUALIZAR_SEGUIMIENTO)
                .numberOfTasks(5)
                .lockDuration(Duration.ofMinutes(2))
                .acquireAndLock();

        if (mostrarSiCero || !trabajos.isEmpty()) {
            System.out.println(
                    "[Worker 2] Trabajos recibidos (topic: " + TOPIC_ACTUALIZAR_SEGUIMIENTO + "): " + trabajos.size());
        }

        for (AcquiredExternalWorkerJob trabajo : trabajos) {
            try {
                Map<String, Object> vars = trabajo.getVariables();

                String elementId = trabajo.getElementId() != null ? trabajo.getElementId() : "";

                // Determinar si la compra fue realizada segun la variable de Flowable o la
                // actividad del BPMN
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
                                        : "Seguimiento finalizado: potencial cliente no concreto la compra"));

                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("compraRealizada", compraRealizada);
                payload.put("clienteEstadoCompra", estadoCompra);
                payload.put("clienteObservaciones", observaciones);

                String jsonBody = mapper.writeValueAsString(payload);
                String ruta = "/api/clientes/" + clienteId + "/seguimiento";
                System.out.println("[Worker 2] Enviando PUT " + ruta + ": " + jsonBody);

                HttpResponse<String> respuesta = enviarPeticionServicio(clienteHttp, "PUT", ruta, jsonBody);
                System.out.println("[Worker 2] Respuesta de " + ruta + " (HTTP " + respuesta.statusCode() + "): "
                        + respuesta.body());

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

    /**
     * WORKER 3 (topic: seleccionar-clientes-campana)
     * Consulta GET /api/clientes y selecciona exclusivamente clientes que
     * autorizaron promociones y cumplen los filtros de la campana.
     * Devuelve a Flowable un booleano, una cantidad y una lista JSON.
     */
    private static void procesarSeleccionarClientesCampana(
            ExternalWorkerClient worker,
            ObjectMapper mapper,
            HttpClient clienteHttp,
            boolean mostrarSiCero) {
        List<AcquiredExternalWorkerJob> trabajos = worker.createJobAcquireBuilder()
                .topic(TOPIC_SELECCIONAR_CAMPANA)
                .numberOfTasks(5)
                .lockDuration(Duration.ofMinutes(2))
                .acquireAndLock();

        if (mostrarSiCero || !trabajos.isEmpty()) {
            System.out.println("[Worker 3] Trabajos recibidos: " + trabajos.size());
        }

        for (AcquiredExternalWorkerJob trabajo : trabajos) {
            try {
                Map<String, Object> vars = trabajo.getVariables();
                String tipoCampana = leerTexto(vars, "tipoCampana", "FECHA_ESPECIAL");
                String mesCumpleanos = leerTexto(vars, "mesCumpleanos", "");
                String especie = leerTexto(vars, "especieMascota", "Todos");
                String tamano = leerTexto(vars, "tamanoMascota", "Todos");

                if (!tipoCampana.equalsIgnoreCase("CUMPLEANOS")
                        && !tipoCampana.equalsIgnoreCase("FECHA_ESPECIAL")) {
                    throw new IllegalArgumentException("tipoCampana no valido: " + tipoCampana);
                }
                if (!mesCumpleanos.isBlank()) {
                    int mes = Integer.parseInt(mesCumpleanos);
                    if (mes < 1 || mes > 12) {
                        throw new IllegalArgumentException("mesCumpleanos debe estar entre 1 y 12");
                    }
                }

                HttpResponse<String> respuesta = enviarPeticionServicio(
                        clienteHttp, "GET", "/api/clientes", null);
                if (respuesta.statusCode() != 200) {
                    reportarFalloFlowable(worker, trabajo,
                            "Error al consultar GET /api/clientes: HTTP " + respuesta.statusCode(),
                            respuesta.body());
                    continue;
                }

                JsonNode clientes = mapper.readTree(respuesta.body());
                if (!clientes.isArray()) {
                    throw new IllegalStateException("GET /api/clientes no devolvio una lista JSON");
                }

                List<Map<String, Object>> seleccionados = new ArrayList<>();
                java.util.Set<Integer> idsIncluidos = new java.util.HashSet<>();

                for (JsonNode cliente : clientes) {
                    if (!cliente.path("CLIENTE_AUTORIZA").asBoolean(false)) {
                        continue;
                    }
                    if (!"Todos".equalsIgnoreCase(especie)
                            && !especie.equalsIgnoreCase(cliente.path("MASCOTA_ESPECIE").asText(""))) {
                        continue;
                    }
                    if (!"Todos".equalsIgnoreCase(tamano)
                            && !tamano.equalsIgnoreCase(cliente.path("MASCOTA_TAMANO").asText(""))) {
                        continue;
                    }
                    // El mes solo se aplica a las campanas de cumpleanos.
                    if (tipoCampana.equalsIgnoreCase("CUMPLEANOS") && !mesCumpleanos.isBlank()) {
                        String fecha = cliente.path("MASCOTA_CUMPLEANO").asText("");
                        if (fecha.isBlank()) {
                            continue;
                        }
                        int mesRegistrado = java.time.LocalDate.parse(fecha).getMonthValue();
                        if (mesRegistrado != Integer.parseInt(mesCumpleanos)) {
                            continue;
                        }
                    }

                    int clienteId = cliente.path("CLIENTE_ID").asInt(0);
                    if (clienteId <= 0 || !idsIncluidos.add(clienteId)) {
                        continue;
                    }
                    Map<String, Object> seleccionado = new LinkedHashMap<>();
                    seleccionado.put("clienteId", clienteId);
                    seleccionado.put("clienteNombre", cliente.path("CLIENTE_NOMBRE").asText(""));
                    seleccionado.put("mascotaNombre", cliente.path("MASCOTA_NOMBRE").asText(""));
                    seleccionados.add(seleccionado);
                }

                boolean hayClientes = !seleccionados.isEmpty();
                // Almacenamos la lista como JSON de texto para su lectura posterior.
                String listaJson = mapper.writeValueAsString(seleccionados);
                worker.createCompletionBuilder(trabajo)
                        .variable("clientesSeleccionados", hayClientes)
                        .variable("cantidadClientesCampana", seleccionados.size())
                        .variable("listaClientesCampana", listaJson)
                        .complete();

                System.out.println("[Worker 3] Trabajo completado. Clientes elegibles: "
                        + seleccionados.size());
            } catch (Exception e) {
                reportarFalloFlowable(worker, trabajo,
                        "Error al ejecutar Worker 3: " + e.getMessage(), e.toString());
            }
        }
    }

    /**
     * WORKER 4 (topic: registrar-seguimiento-campana)
     * Recibe los resultados consignados en FormTask_27, verifica que clienteId
     * pertenezca a listaClientesCampana y actualiza el seguimiento mediante
     * PUT /api/clientes/{id}/seguimiento.
     * Importante: este endpoint actualiza el seguimiento comercial existente;
     * no crea un historial independiente de campanas.
     */
    private static void procesarSeguimientoCampana(
            ExternalWorkerClient worker,
            ObjectMapper mapper,
            HttpClient clienteHttp,
            boolean mostrarSiCero) {
        List<AcquiredExternalWorkerJob> trabajos = worker.createJobAcquireBuilder()
                .topic(TOPIC_SEGUIMIENTO_CAMPANA)
                .numberOfTasks(5)
                .lockDuration(Duration.ofMinutes(2))
                .acquireAndLock();

        if (mostrarSiCero || !trabajos.isEmpty()) {
            System.out.println("[Worker 4] Trabajos recibidos: " + trabajos.size());
        }

        for (AcquiredExternalWorkerJob trabajo : trabajos) {
            try {
                Map<String, Object> vars = trabajo.getVariables();
                if (vars == null) {
                    throw new IllegalArgumentException("No se recibieron variables del proceso");
                }

                // No se usa un cliente por defecto: se evita actualizar a otro cliente.
                int clienteId = leerEntero(vars, "clienteId", 0);
                if (clienteId <= 0) {
                    throw new IllegalArgumentException("FormTask_27 debe indicar un clienteId valido");
                }

                // Worker 3 devuelve esta lista como texto JSON.
                String listaJson = leerTexto(vars, "listaClientesCampana", "");
                if (listaJson.isBlank()) {
                    throw new IllegalArgumentException("Falta listaClientesCampana del Worker 3");
                }
                JsonNode listaSeleccionados = mapper.readTree(listaJson);
                if (!listaSeleccionados.isArray()) {
                    throw new IllegalArgumentException("listaClientesCampana no es una lista JSON");
                }
                boolean clienteSeleccionado = false;
                for (JsonNode item : listaSeleccionados) {
                    if (item.path("clienteId").asInt(0) == clienteId) {
                        clienteSeleccionado = true;
                        break;
                    }
                }
                if (!clienteSeleccionado) {
                    throw new IllegalArgumentException(
                            "clienteId " + clienteId + " no pertenece a listaClientesCampana");
                }

                String tipoCampana = leerTexto(vars, "tipoCampana", "");
                if (tipoCampana.isBlank()) {
                    throw new IllegalArgumentException("Falta tipoCampana");
                }
                boolean promocionEnviada = leerBooleanoObligatorio(vars, "promocionEnviada");
                boolean compraRealizada = leerBooleanoObligatorio(vars, "compraRealizadaCampana");
                String respuestaCliente = leerTexto(vars, "respuestaClienteCampana", "");
                if (respuestaCliente.isBlank()) {
                    throw new IllegalArgumentException("Falta respuestaClienteCampana");
                }
                String observaciones = leerTexto(vars, "observacionesCampana", "");

                // El endpoint REST exige compraRealizada y estos campos de texto.
                String estado = compraRealizada
                        ? "Campana: compra realizada"
                        : "Campana: sin compra";
                String detalle = "Tipo de campana: " + tipoCampana
                        + "; promocion enviada: " + promocionEnviada
                        + "; respuesta del cliente: " + respuestaCliente
                        + (observaciones.isBlank() ? "" : "; observaciones: " + observaciones);

                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("compraRealizada", compraRealizada);
                payload.put("clienteEstadoCompra", estado);
                payload.put("clienteObservaciones", detalle);

                String ruta = "/api/clientes/" + clienteId + "/seguimiento";
                HttpResponse<String> respuesta = enviarPeticionServicio(
                        clienteHttp, "PUT", ruta, mapper.writeValueAsString(payload));

                if (respuesta.statusCode() == 200 || respuesta.statusCode() == 201) {
                    worker.createCompletionBuilder(trabajo)
                            .variable("seguimientoCampanaRegistrado", true)
                            .variable("estadoCampana", "Finalizada")
                            .complete();
                    System.out.println("[Worker 4] Seguimiento registrado. Cliente ID: " + clienteId);
                } else {
                    reportarFalloFlowable(worker, trabajo,
                            "Error en PUT " + ruta + ": HTTP " + respuesta.statusCode(),
                            respuesta.body());
                }
            } catch (Exception e) {
                reportarFalloFlowable(worker, trabajo,
                        "Error al ejecutar Worker 4: " + e.getMessage(), e.toString());
            }
        }
    }

    /** Exige un valor booleano explicito para no inventar resultados. */
    private static boolean leerBooleanoObligatorio(Map<String, Object> vars, String clave) {
        Object valor = vars.get(clave);
        if (valor instanceof Boolean b) {
            return b;
        }
        if (valor != null) {
            String texto = String.valueOf(valor).trim();
            if (texto.equalsIgnoreCase("true")) {
                return true;
            }
            if (texto.equalsIgnoreCase("false")) {
                return false;
            }
        }
        throw new IllegalArgumentException("Falta un valor booleano valido para " + clave);
    }

    private static void reportarFalloFlowable(
            ExternalWorkerClient worker,
            AcquiredExternalWorkerJob trabajo,
            String mensajeError,
            String detalleError) {
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
            String jsonBody) throws Exception {

        String baseConfigurada = System.getenv("SERVICIO_PELUDITOS_URL");

        if (baseConfigurada == null || baseConfigurada.isBlank()) {
            throw new IllegalStateException(
                    "Debes configurar SERVICIO_PELUDITOS_URL.");
        }

        String basePrincipal = baseConfigurada.trim();

        // Si la direccion incluye /api/, conservar solamente la URL base.
        int idxApi = basePrincipal.indexOf("/api/");
        if (idxApi > 0) {
            basePrincipal = basePrincipal.substring(0, idxApi);
        }

        // Evitar una doble barra al construir la direccion.
        while (basePrincipal.endsWith("/")) {
            basePrincipal = basePrincipal.substring(0, basePrincipal.length() - 1);
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(basePrincipal + ruta))
                .version(HttpClient.Version.HTTP_1_1)
                .timeout(Duration.ofSeconds(5));

        if ("POST".equalsIgnoreCase(metodo)) {
            builder.header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            jsonBody != null ? jsonBody : "{}"));
        } else if ("PUT".equalsIgnoreCase(metodo)) {
            builder.header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(
                            jsonBody != null ? jsonBody : "{}"));
        } else {
            builder.GET();
        }

        return cliente.send(
                builder.build(),
                HttpResponse.BodyHandlers.ofString());
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