package no.mesan.auth;

import io.dropwizard.auth.AuthenticationException;

import io.dropwizard.java8.auth.Authenticator;
import io.jsonwebtoken.*;
import no.mesan.config.FriskConfiguration;
import no.mesan.dao.UserDao;
import no.mesan.model.User;

import java.util.Optional;

public class OpenIdAuthenticator implements Authenticator<String, User> {
    private UserDao userDao;
    private String jwtSecret;

    public OpenIdAuthenticator(UserDao userDao, String jwtSecret) {
        this.userDao = userDao;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Optional<User> authenticate(String token) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return userDao.getUserByEmail((String)claims.get("email"));
        } catch (MalformedJwtException | ExpiredJwtException | SignatureException e) {
            return Optional.empty();
        }
    }
}
