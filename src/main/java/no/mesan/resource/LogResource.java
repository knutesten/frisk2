package no.mesan.resource;

import io.dropwizard.auth.Auth;
import no.mesan.model.Type;
import no.mesan.model.User;
import no.mesan.service.LogService;

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
    public Response getLog() {
        return Response.ok(logService.getLog()).build();
    }

    @POST
    public Response insert(@Auth User user, Type type) {
        return Response.ok(logService.insert(user, type)).build();
    }
}
