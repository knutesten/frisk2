package no.mesan.resource;
import no.mesan.auth.AuthenticationService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/auth")
public class LoginResource {
    private AuthenticationService authenticationService = new AuthenticationService();
    @GET
    @Path("/login")
    public Response redirectToConnectIdService() {
        return Response.temporaryRedirect(authenticationService.createLoginUrl()).build();
    }

    @GET
    @Path("/code")
    public Response handleCode(@QueryParam("code") String code) {
        return Response.ok(authenticationService.exchangeCodeForAccessToken(code)).build();
    }
}
