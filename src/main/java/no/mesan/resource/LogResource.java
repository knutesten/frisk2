package no.mesan.resource;

import no.mesan.service.LogService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/log")
@Produces(MediaType.APPLICATION_JSON)
public class LogResource {
    private LogService logService;

    public LogResource(LogService logService) {
        this.logService = logService;
    }

    @GET
    public Response getLog() {
        return Response.ok(logService.getLog()).build();
    }
}
