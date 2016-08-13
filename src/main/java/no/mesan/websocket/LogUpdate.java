package no.mesan.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/update")
public class LogUpdate {
    private static Set<Session> sessions = ConcurrentHashMap.newKeySet();

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    public static void updateClients() {
        try {
            for (Session s : sessions) s.getBasicRemote().sendText("update");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
