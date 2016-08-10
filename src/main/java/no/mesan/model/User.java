package no.mesan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.Principal;

public class User implements Principal{
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String username;

    public User(int id, String firstName, String lastName, String email, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getName() {
        return username;
    }
}
