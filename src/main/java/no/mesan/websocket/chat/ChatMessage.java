package no.mesan.websocket.chat;

import no.mesan.model.User;

import javax.websocket.Session;

public class ChatMessage implements Message {
    private User sender;
    private String message;

    public ChatMessage(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }

    @Override
    public void accept(Session session, MessageVisitor messageVisitor) {
        messageVisitor.visit(session, this);
    }
}
