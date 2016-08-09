package no.mesan.auth;

import io.dropwizard.auth.AuthenticationException;

import io.dropwizard.java8.auth.Authenticator;
import io.jsonwebtoken.*;
import no.mesan.dao.UserDao;
import no.mesan.model.User;

import java.util.Optional;

public class OpenIdAuthenticator implements Authenticator<String, User> {
    private UserDao userDao;

    public OpenIdAuthenticator(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(token)
                    .getBody();
            return userDao.getUserByEmail((String)claims.get("email"));
        } catch (MalformedJwtException | ExpiredJwtException | SignatureException e) {
            return Optional.empty();
        }
    }
}
