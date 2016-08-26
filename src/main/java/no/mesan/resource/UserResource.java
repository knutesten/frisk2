package no.mesan.resource;

import io.dropwizard.auth.Auth;
import no.mesan.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("session")
public class UserResource {
    @GET
    public Response getSession(@Auth User user) {
        return Response.ok(user).build();
    }
}
