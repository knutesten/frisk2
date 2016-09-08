package no.mesan.websocket.chat;

import no.mesan.auth.OpenIdAuthenticator;
import no.mesan.model.User;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

class MessageVisitor {
    private final Map<Session, User> sessions;
    private OpenIdAuthenticator authenticator;

    MessageVisitor(Map<Session, User> sessions, OpenIdAuthenticator authenticator) {
        this.sessions = sessions;
        this.authenticator = authenticator;
    }

    void visit(Session session, ChatMessage chatMessage) {
        if (!sessions.containsKey(session)) {
            closeSession(session);
            return;
        }

        broadcastMessage(new ChatMessage(
                sessions.get(session),
                chatMessage.getMessage()
        ));
    }

    void visit(Session session, AuthMessage authMessage) {
        Optional<User> user = authenticator.authenticate(authMessage.getToken());
        if (user.isPresent()) {
            sessions.put(session, user.get());
        } else {
            closeSession(session);
        }
    }

    private void broadcastMessage(ChatMessage message) {
        sessions.keySet().forEach(
                s -> {
                    try {
                        s.getBasicRemote().sendObject(message);
                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
