package no.mesan.resource;
import no.mesan.auth.DiscoveryDocument;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by knutn on 7/21/2016.
 */
@Path("/login")
public class LoginResource {
    private DiscoveryDocument discoveryDocument = new DiscoveryDocument();

    @GET
    public Response login() throws MalformedURLException {
        URI loginUrl = UriBuilder.fromUri(discoveryDocument.getAuthorizationEndpoint())
                .queryParam("client_id", "241147659016-791b524s2vffs0ogt4mnoq2ole988tcu.apps.googleusercontent.com")
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email")
                .queryParam("redirect_uri", "http://localhost:8080/openid")
                .queryParam("state", "teststate").build();

        return Response.status(Response.Status.FOUND).location(loginUrl).build();
    }
}
