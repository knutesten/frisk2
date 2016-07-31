package no.mesan.auth;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.Jwts;
import no.mesan.model.User;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class AuthenticationService {
    private DiscoveryDocument discoveryDocument = new DiscoveryDocument();

    public URI createLoginUrl() {
        return UriBuilder.fromUri(discoveryDocument.getAuthorizationEndpoint())
                .queryParam("client_id", discoveryDocument.getClientId())
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email")
                .queryParam("redirect_uri", "http://localhost:8080/auth/code").build();

    }

    public User exchangeCodeForAccessToken(String code) {
        // TODO: Check that state equals that of the request.

        Form form = new Form()
                .param("code", code)
                .param("client_id", discoveryDocument.getClientId())
                .param("client_secret", discoveryDocument.getClientSecret())
                .param("grant_type", "authorization_code")
                .param("redirect_uri", "http://localhost:8080/auth/code");

        Response response = ClientBuilder.newClient().target(discoveryDocument.getTokenEndpoint())
                .request()
                .post(Entity.form(form));

        String idToken = response.readEntity(JsonNode.class).get("id_token").asText();
        Jwts.parser().setSigningKeyResolver(new FriskSigningKeyResolver()).parseClaimsJws(idToken).getBody().get("email", String.class);

        return null;
    }
}
