package cl.iplacex.peluditos;

import java.nio.file.Files;
import java.nio.file.Path;
import io.javalin.http.staticfiles.Location;
import java.util.Map;
import io.javalin.Javalin;
import io.javalin.util.JavalinBindException;
import cl.iplacex.peluditos.BaseDatos.RegistroClienteRequest;

public class App {

    public static void main(String[] args) {
        BaseDatos.inicializar();

        int puertoPreferido = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));
        Javalin app = crearServidor();

        try {
            app.start(puertoPreferido);
            System.out.println("Servicio REST Peluditos iniciado en http://localhost:" + puertoPreferido);
        } catch (JavalinBindException e) {
            System.out.println("Puerto " + puertoPreferido + " en uso, iniciando automaticamente en puerto 7071...");
            app = crearServidor();
            app.start(7071);
            System.out.println("Servicio REST Peluditos iniciado en http://localhost:7071");
        }
    }

    private static Javalin crearServidor() {
                Path interfaz = Path.of("..", "interfaz-peluditos").toAbsolutePath().normalize();

        Javalin app = Javalin.create(config -> {
            if (Files.isDirectory(interfaz)) {
                config.staticFiles.add(archivos -> {
                    archivos.hostedPath = "/interfaz";
                    archivos.directory = interfaz.toString();
                    archivos.location = Location.EXTERNAL;
                });
            }
        });

        // Endpoint de estado / seguimiento comercial
        app.get("/api/seguimiento", ctx -> {
            ctx.json(new Mensaje(
                "OK",
                "Servicio de seguimiento y registro de clientes de Peluditos Pet Chile funcionando"
            ));
        });

        // Endpoint para guardar potencial cliente y su mascota (usado por el External Worker guardar-cliente-peluditos)
        app.post("/api/clientes", ctx -> {
            RegistroClienteRequest req = ctx.bodyAsClass(RegistroClienteRequest.class);
            Map<String, Object> respuesta = BaseDatos.guardarClienteYMascota(req);
            ctx.status(201).json(respuesta);
        });

        // Endpoint para consultar todos los clientes y mascotas guardados en la BD
        app.get("/api/clientes", ctx -> {
            ctx.json(BaseDatos.listarClientes());
        });

        // Endpoint para consultar el catalogo de productos y tipos de mascota
        app.get("/api/productos", ctx -> {
            ctx.json(BaseDatos.listarProductos());
        });

        return app;
    }

    public record Mensaje(String estado, String mensaje) {}
}
