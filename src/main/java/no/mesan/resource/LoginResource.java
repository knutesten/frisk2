package no.mesan.resource;

import no.mesan.auth.AuthenticationService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/auth")
@Produces(APPLICATION_JSON)
public class LoginResource {
    private AuthenticationService authenticationService;

    public LoginResource(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GET
    @Path("/login")
    public Response redirectToConnectIdService() {
        return Response.ok(authenticationService.createLoginUrl()).build();
    }

    @GET
    @Path("/token")
    public Response exchangeCodeForToken(@QueryParam("code") String code, @QueryParam("state") String state) {
        final Optional<String> token = authenticationService.exchangeCodeForAccessToken(code, state);
        return token.isPresent() ?
                Response.ok(new HashMap<String, Object>() {{
                    put("jwtToken", token.get());
                }}).build() :
                Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
