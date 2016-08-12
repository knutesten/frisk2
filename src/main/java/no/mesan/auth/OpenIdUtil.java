package no.mesan.auth;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.impl.Base64Codec;
import no.mesan.config.OpenIdConfiguration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.net.URI;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class OpenIdUtil {
    private final OpenIdConfiguration config;
    private static JsonNode discoveryDocument;
    private static JsonNode jwks;
    private byte[] jwtSecret;

    public OpenIdUtil(OpenIdConfiguration openIdConfiguration) {
        this.config = openIdConfiguration;

        if (discoveryDocument == null) {
            Response response = ClientBuilder.newClient().target(config.getDiscoveryDocumentUrl()).request().get();
            discoveryDocument = response.readEntity(JsonNode.class);
        }

        if (jwks == null) {
            Response response = ClientBuilder.newClient().target(discoveryDocument.get("jwks_uri").asText()).request().get();
            jwks = response.readEntity(JsonNode.class).get("keys");
        }
    }

    URI getAuthorizationEndpoint() {
        return URI.create(discoveryDocument.get("authorization_endpoint").asText());
    }

    URI getTokenEndpoint() {
        return URI.create(discoveryDocument.get("token_endpoint").asText());
    }

    String getClientId() {
        return config.getClientId();
    }

    String getClientSecret() {
        return config.getClientSecret();
    }

    String getRedirectUri() {
        return config.getRedirectUri();
    }

    Key getKeyFromKIDandAlg(String kid, String alg) {
        for (JsonNode jwk : jwks)
            if (jwk.get("kid").asText().equals(kid) && jwk.get("alg").asText().equals(alg))
                return createKey(jwk.get("n").asText(), jwk.get("e").asText());

        throw new IllegalArgumentException("KID does not match any of the keys in the discovery document.");
    }

    private Key createKey(String n, String e) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(
                    new BigInteger(1, Base64Codec.BASE64URL.decode(n)),
                    new BigInteger(1, Base64Codec.BASE64URL.decode(e))
            ));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    String getJwtSecret() {
        return config.getJwtSecret();
    }
}
