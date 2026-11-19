package cl.iplacex.peluditos;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;

@Path("/api/consultas")
public class ConsultasResource {
    @POST
@Path("/{referencia}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response registrarConsulta(
        @PathParam("referencia") String referencia,
        JsonNode datos) {

    if (datos == null || !datos.isObject()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Map.of(
                        "mensaje", "Los datos del formulario no son válidos."
                ))
                .build();
    }

    try {
        java.util.UUID.fromString(referencia);
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Map.of(
                        "mensaje", "La referencia del registro no es válida."
                ))
                .build();
    }

    return new RegistroFlowableJakarta().registrar(referencia, datos);
}

}