package no.mesan.auth;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.impl.Base64Codec;
import no.mesan.config.OpenIdConfiguration;

import java.math.BigInteger;
import java.net.URI;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class OpenIdUtil {
    private final OpenIdConfiguration config;
    private final DiscoveryDocumentCache cache;

    public OpenIdUtil(OpenIdConfiguration openIdConfiguration, DiscoveryDocumentCache discoveryDocumentCache) {
        this.config = openIdConfiguration;
        this.cache = discoveryDocumentCache;
    }

    URI getAuthorizationEndpoint() {
        return URI.create(cache.getDiscoveryDocument().get("authorization_endpoint").asText());
    }

    URI getTokenEndpoint() {
        return URI.create(cache.getDiscoveryDocument().get("token_endpoint").asText());
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

    String getJwtSecret() {
        return config.getJwtSecret();
    }


    Key getKeyFromKidAndAlg(String kid, String alg) {
        for (JsonNode jwk : cache.getJwksKeys())
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
}
