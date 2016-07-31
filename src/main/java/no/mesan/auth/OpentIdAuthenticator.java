package no.mesan.auth;

import io.dropwizard.auth.AuthenticationException;

import io.dropwizard.java8.auth.Authenticator;
import no.mesan.model.User;

import java.util.Optional;

/**
 * Created by knutn on 7/21/2016.
 */
public class OpentIdAuthenticator implements Authenticator<String, User> {
    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        System.out.println("Token: " + token);

        return Optional.of(null);
    }
}
