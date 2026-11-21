package cl.iplacex.peluditos;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PathParam;

@Path("/api/clientes")
public class ClientesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarClientes() {
        try {
            return Response.ok(BaseDatos.listarClientes()).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new Mensaje("ERROR", "No se pudieron consultar los clientes"))
                    .build();
        }
    }
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response registrarCliente(BaseDatos.RegistroClienteRequest datos) {
    try {
        return Response.status(Response.Status.CREATED)
                .entity(BaseDatos.guardarClienteYMascota(datos))
                .build();
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Mensaje("ERROR", e.getMessage()))
                .build();
    } catch (Exception e) {
        return Response.serverError()
                .entity(new Mensaje("ERROR", "No se pudo registrar el cliente"))
                .build();
    }
}    

@PUT
@Path("/{id}/seguimiento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response actualizarSeguimiento(
        @PathParam("id") int clienteId,
        SeguimientoRequest datos) {

    if (datos == null || datos.compraRealizada() == null) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Mensaje("ERROR", "Debe indicar compraRealizada"))
                .build();
    }

    try {
        return Response.ok(BaseDatos.actualizarSeguimiento(
                clienteId,
                datos.compraRealizada(),
                datos.clienteEstadoCompra(),
                datos.clienteObservaciones()
        )).build();

    } catch (IllegalArgumentException e) {
        return Response.status(
                clienteId <= 0
                        ? Response.Status.BAD_REQUEST
                        : Response.Status.NOT_FOUND)
                .entity(new Mensaje("ERROR", e.getMessage()))
                .build();

    } catch (Exception e) {
        return Response.serverError()
                .entity(new Mensaje("ERROR", "No se pudo actualizar el seguimiento"))
                .build();
    }
}

public record SeguimientoRequest(
        Boolean compraRealizada,
        String clienteEstadoCompra,
        String clienteObservaciones
) {}

    public record Mensaje(String estado, String mensaje) {}
}