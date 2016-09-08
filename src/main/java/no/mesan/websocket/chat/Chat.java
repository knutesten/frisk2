package no.mesan.websocket.chat;

import no.mesan.model.User;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/api/chat",
        decoders = {AuthMessageDecoder.class, ChatMessageDecoder.class},
        encoders = {ChatMessageEncoder.class}
)
public class Chat {
    private static Map<Session, User> sessions = new ConcurrentHashMap<>();
    private MessageVisitor messageVisitor = new MessageVisitor(sessions, null);

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        message.accept(session, messageVisitor);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

}
