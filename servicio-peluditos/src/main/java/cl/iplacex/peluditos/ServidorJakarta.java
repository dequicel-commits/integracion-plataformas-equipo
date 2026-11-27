package cl.iplacex.peluditos;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class ServidorJakarta {

        public static void main(String[] args) throws Exception {

                BaseDatos.inicializar();

                String host = System.getenv().getOrDefault("PELU_HOST", "localhost");
                String puerto = System.getenv().getOrDefault("PELU_PORT", "7072");

                URI direccion = URI.create("http://" + host + ":" + puerto + "/");

                ResourceConfig configuracion = new ResourceConfig()
                                .packages("cl.iplacex.peluditos")
                                .register(JacksonFeature.class);

                HttpServer servidor = GrizzlyHttpServerFactory.createHttpServer(
                                direccion,
                                configuracion,
                                false);

                
                           String rutaInterfaz = System.getenv().getOrDefault(
                      "PELU_INTERFAZ_PATH", "../interfaz-peluditos");

                          Path carpetaInterfaz = Path.of(rutaInterfaz)
                          .toAbsolutePath()
                          .normalize();


                if (Files.isDirectory(carpetaInterfaz)) {

                        StaticHttpHandler archivos = new StaticHttpHandler(
                                        carpetaInterfaz.toString());

                        servidor.getServerConfiguration()
                                        .addHttpHandler(archivos, "/interfaz/");

                        System.out.println(
                                        "Interfaz disponible en " + direccion + "interfaz/index.html");

                } else {
                        System.out.println(
                                        "No se encontró la carpeta interfaz-peluditos.");
                }

                servidor.start();

                System.out.println(
                                "Servidor Jakarta REST iniciado en " + direccion);

                Runtime.getRuntime().addShutdownHook(
                                new Thread(servidor::shutdownNow));

                Thread.currentThread().join();
        }
}
