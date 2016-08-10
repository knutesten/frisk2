package no.mesan.model;

public class Type {
    private int id;
    private String name;
    private int amount;

    public Type(int id, String name, int amount) {
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
