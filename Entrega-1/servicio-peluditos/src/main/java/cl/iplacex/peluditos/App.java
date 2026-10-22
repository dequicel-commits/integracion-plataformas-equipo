package cl.iplacex.peluditos;

import io.javalin.Javalin;

public class App {

    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7070);

        app.get("/api/seguimiento", ctx -> {
            ctx.json(new Mensaje(
                "OK",
                "Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"
            ));
        });

        System.out.println("Servicio REST iniciado en puerto 7070");
    }

    public record Mensaje(String estado, String mensaje) {}
}
