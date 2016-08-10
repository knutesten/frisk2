package no.mesan.model;

import java.time.ZonedDateTime;

public class LogEntry {
    private int id;
    private ZonedDateTime date;
    private User user;
    private Type type;

    public LogEntry(int id, ZonedDateTime date, User user, Type type) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public Type getType() {
        return type;
    }
}
