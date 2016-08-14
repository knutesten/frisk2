package no.mesan.resource;

import io.dropwizard.auth.Auth;
import no.mesan.model.Type;
import no.mesan.model.User;
import no.mesan.service.LogService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/log")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class LogResource {
    private LogService logService;

    public LogResource(LogService logService) {
        this.logService = logService;
    }

    @GET
    @RolesAllowed("USER")
    public Response getLog() {
        return Response.ok(logService.getLog()).build();
    }

    @GET
    @Path("today")
    public Response getTodaysConsumption(@Auth User user) {
        return Response.ok(logService.getTodaysConsumption(user)).build();
    }

    @POST
    public Response insert(@Auth User user, Type type) {
        return Response.ok(logService.insert(user, type)).build();
    }

    @DELETE
    @Path("/undo")
    public Response undo(@Auth User user) {
        logService.delete(user);
        return Response.noContent().build();
    }
}
