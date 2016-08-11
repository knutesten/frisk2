package no.mesan.model;

import java.time.Instant;

public class LogEntry {
    private int id;
    private Instant date;
    private User user;
    private Type type;

    public LogEntry(int id, Instant date, User user, Type type) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Instant getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public Type getType() {
        return type;
    }
}
