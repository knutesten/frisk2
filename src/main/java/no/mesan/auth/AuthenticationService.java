package no.mesan.auth;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.*;
import no.mesan.dao.UserDao;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.security.Key;
import java.util.Optional;

public class AuthenticationService {
    private OpenIdUtil openIdUtil;
    private UserDao userDao;

    private SigningKeyResolver signingKeyResolver= new SigningKeyResolver() {
        @Override
        public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
            return resolveSigningKey(jwsHeader, "");
        }
        @Override
        public Key resolveSigningKey(JwsHeader jwsHeader, String s) {
            return openIdUtil.getKeyFromKIDandAlg(jwsHeader.getKeyId(), jwsHeader.getAlgorithm());
        }
    };

    public AuthenticationService(OpenIdUtil openIdUtil, UserDao userDao) {
        this.openIdUtil = openIdUtil;
        this.userDao = userDao;
    }

    public URI createLoginUrl() {
        return UriBuilder.fromUri(openIdUtil.getAuthorizationEndpoint())
                .queryParam("client_id", openIdUtil.getClientId())
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email")
                .queryParam("state", "state")
                .queryParam("redirect_uri", openIdUtil.getRedirectUri()).build();
    }

    public Optional<String> exchangeCodeForAccessToken(String code, String state) {
        // TODO: Check that state equals that of the request.

        Form form = new Form()
                .param("code", code)
                .param("client_id", openIdUtil.getClientId())
                .param("client_secret", openIdUtil.getClientSecret())
                .param("grant_type", "authorization_code")
                .param("redirect_uri", openIdUtil.getRedirectUri());

        Response response = ClientBuilder.newClient().target(openIdUtil.getTokenEndpoint())
                .request()
                .post(Entity.form(form));

        if (response.getStatusInfo().equals(Response.Status.OK)) {
            String idToken = response.readEntity(JsonNode.class).get("id_token").asText();
            String email = Jwts.parser()
                    .setSigningKeyResolver(signingKeyResolver)
                    .parseClaimsJws(idToken)
                    .getBody()
                    .get("email", String.class);

            return userDao.getUserByEmail(email).isPresent() ?
                    Optional.of(Jwts.builder()
                            .claim("email", email)
                            .signWith(SignatureAlgorithm.HS512, "secret")
                            .compact()):
                    Optional.empty();
        }

        return Optional.empty();
    }
}
