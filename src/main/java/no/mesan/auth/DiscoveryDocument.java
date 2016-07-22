package no.mesan.auth;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.impl.Base64Codec;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.net.URI;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by knutn on 7/21/2016.
 */
// TODO: Make me beautiful (ie. get the static finals strings/URIs into the config-file and find a way to initiate class without having to pass the variables to LoginResource).
public class DiscoveryDocument {
    private static JsonNode discoveryDocument;
    private static JsonNode jwks;
    // TODO: Put these in frisk.yml
    private static final URI GOOGLE_DISCOVERY_DOCUMENT_URL = URI.create("https://accounts.google.com/.well-known/openid-configuration");
    private static final String CLIENT_ID = "241147659016-791b524s2vffs0ogt4mnoq2ole988tcu.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "G3CzwAZZuuKIh_h-jJ6Qtptz";

    public DiscoveryDocument() {
        if (discoveryDocument == null) {
            Response response = ClientBuilder.newClient().target(GOOGLE_DISCOVERY_DOCUMENT_URL).request().get();
            discoveryDocument = response.readEntity(JsonNode.class);
        }

        if (jwks == null) {
            Response response = ClientBuilder.newClient().target(discoveryDocument.get("jwks_uri").asText()).request().get();
            jwks = response.readEntity(JsonNode.class).get("keys");
        }
    }

    public URI getAuthorizationEndpoint() {
        return URI.create(discoveryDocument.get("authorization_endpoint").asText());
    }

    public URI getTokenEndpoint() {
        return URI.create(discoveryDocument.get("token_endpoint").asText());
    }

    public String getClientId() {
        return CLIENT_ID;
    }

    public String getClientSecret() {
        return CLIENT_SECRET;
    }

    public Key getKeyFromKIDandAlg(String kid, String alg) {
        for (int i = 0; i < jwks.size(); i++) {
            JsonNode jwk = jwks.get(i);
            if (jwk.get("kid").asText().equals(kid) && jwk.get("alg").asText().equals(alg)) {
                return createKey(jwk.get("n").asText(), jwk.get("e").asText());
            }
        }

        throw new IllegalArgumentException("KID does not match any of the keys in the discovery document.");
    }

    private Key createKey(String n, String e) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(
                    new BigInteger(1, Base64Codec.BASE64URL.decode(n)),
                    new BigInteger(1, Base64Codec.BASE64URL.decode(e))
            ));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
