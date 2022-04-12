package com.hsiao.springboot.websocket.dist.session;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

/**
 * 一个用户对应多个session
 */
public class MultiSessionManager implements SessionManager {

    private ConcurrentHashMap<String, List<WebSocketSession>> clientSessions = new ConcurrentHashMap();


    @Override
    public List<WebSocketSession> getSession(String id) {
        if (clientSessions.containsKey(id)) {
            return clientSessions.get(id);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WebSocketSession> getAllSessions() {
        return null;
    }

    @Override
    public Set<String> getAllClientIds() {
        return null;
    }


    @Override
    public boolean connect(String id, WebSocketSession session) {
        synchronized (this) {
            if (!clientSessions.containsKey(id)) {
                clientSessions.put(id, new ArrayList<>());
            }
        }

        List<WebSocketSession> sessions = clientSessions.get(id);
        if (!sessions.contains(session)) {
            clientSessions.get(id).add(session);
        }

        return true;
    }

    @Override
    public boolean disconnect(WebSocketSession session) {

        Iterator<List<WebSocketSession>> iterator = clientSessions.values().iterator();
        while (iterator.hasNext()) {
            List<WebSocketSession> sessions = iterator.next();
            if (sessions.contains(session)) {
                sessions.remove(session);
                break;
            }
        }

        return true;
    }

    @Override
    public boolean disconnect(String id) {
        if (clientSessions.containsKey(id)) {
            clientSessions.remove(id);
        }
        return true;
    }

    @Override
    public void clearAll() {
        clientSessions.values().clear();
    }
}
