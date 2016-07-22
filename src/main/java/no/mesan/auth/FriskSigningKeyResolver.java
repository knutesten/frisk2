package no.mesan.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;

import java.security.Key;

/**
 * Created by knutn on 7/22/2016.
 */
public class FriskSigningKeyResolver implements SigningKeyResolver {
    private DiscoveryDocument discoveryDocument = new DiscoveryDocument();

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        return resolveSigningKey(jwsHeader, "");
    }

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, String s) {
        return discoveryDocument.getKeyFromKIDandAlg(jwsHeader.getKeyId(), jwsHeader.getAlgorithm());
    }
}
