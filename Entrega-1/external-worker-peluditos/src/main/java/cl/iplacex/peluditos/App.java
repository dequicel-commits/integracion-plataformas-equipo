package cl.iplacex.peluditos;

import java.time.Duration;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.flowable.external.client.AcquiredExternalWorkerJob;
import org.flowable.external.client.ExternalWorkerClient;
import org.flowable.external.client.impl.JavaHttpClientRestInvoker;
import org.flowable.external.client.impl.RestExternalWorkerClient;

public class App {

    public static void main(String[] args) throws Exception {

        String usuario = System.getenv("FLOWABLE_USER");
        String clave = System.getenv("FLOWABLE_PASSWORD");
        String url = System.getenv("FLOWABLE_API_URL");

        if (usuario == null || usuario.isBlank()
                || clave == null || clave.isBlank()
                || url == null || url.isBlank()) {
            System.out.println("Falta configurar la conexion con Flowable.");
            return;
        }

        ExternalWorkerClient worker = RestExternalWorkerClient.create(
                "peluditos-worker",
                JavaHttpClientRestInvoker.withBasicAuth(url, usuario, clave)
        );

        List<AcquiredExternalWorkerJob> trabajos =
                worker.createJobAcquireBuilder()
                        .topic("seguimiento-peluditos")
                        .numberOfTasks(1)
                        .lockDuration(Duration.ofMinutes(2))
                        .acquireAndLock();

        System.out.println("Trabajos recibidos: " + trabajos.size());

        for (AcquiredExternalWorkerJob trabajo : trabajos) {

            HttpClient cliente = HttpClient.newHttpClient();

            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7070/api/seguimiento"))
                    .GET()
                    .build();

            HttpResponse<String> respuesta = cliente.send(
                    solicitud,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Respuesta del servicio: " + respuesta.body());

            if (respuesta.statusCode() == 200) {
                worker.createCompletionBuilder(trabajo).complete();
                System.out.println("Trabajo completado en Flowable.");
            } else {
                System.out.println("El servicio no respondio correctamente.");
            }
        }
    }
}
