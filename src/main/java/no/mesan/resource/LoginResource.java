package no.mesan.resource;
import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.Jwts;
import no.mesan.auth.AuthenticationService;
import no.mesan.auth.DiscoveryDocument;
import no.mesan.auth.FriskSigningKeyResolver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by knutn on 7/21/2016.
 */
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
