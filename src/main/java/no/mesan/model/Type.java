package no.mesan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Type {
    private int id;
    private String name;
    private int amount;

    public Type(@JsonProperty("id") int id,
                @JsonProperty("name") String name,
                @JsonProperty("amount") int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
