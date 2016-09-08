package no.mesan.websocket.chat;

import javax.websocket.Session;

public class AuthMessage implements Message {
    private String token;

    public AuthMessage(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public void accept(Session session, MessageVisitor messageVisitor) {
        messageVisitor.visit(session, this);
    }
}
