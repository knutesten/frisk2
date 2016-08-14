package no.mesan.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/update")
public class LogUpdate {
    private static Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    public LogUpdate() {
        scheduledExecutorService.scheduleAtFixedRate(LogUpdate::keepConnectionsAlive, 0, 10, TimeUnit.SECONDS);
    }

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

    private static void keepConnectionsAlive() {
        sendTextToAllSessions("heartbeat");
    }

    public static void updateClients() {
        sendTextToAllSessions("update");
    }

    private static void sendTextToAllSessions(String text) {
        try {
            for (Session s : sessions) s.getBasicRemote().sendText(text);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
