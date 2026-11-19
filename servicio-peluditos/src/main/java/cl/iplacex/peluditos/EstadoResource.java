package cl.iplacex.peluditos;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/seguimiento")
public class EstadoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje consultarEstado() {
        return new Mensaje(
            "OK",
            "Servicio Jakarta REST de Peluditos Pet Chile funcionando"
        );
    }

    public record Mensaje(String estado, String mensaje) {}
}
