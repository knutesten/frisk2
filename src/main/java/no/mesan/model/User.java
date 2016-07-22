package no.mesan.model;

import java.security.Principal;

/**
 * Created by knutn on 7/21/2016.
 */
public class User implements Principal{
    private String email;
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
