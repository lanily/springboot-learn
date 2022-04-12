package com.hsiao.springboot.websocket.dist.session;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

/**
 * 一个用户对应一个session
 */
public class SingleSessionManager implements SessionManager {

    protected ConcurrentHashMap<String, WebSocketSession> clientSessions = new ConcurrentHashMap();


    @Override
    public List<WebSocketSession> getSession(String id) {
        List<WebSocketSession> sessions = new ArrayList<>();
        if (clientSessions.containsKey(id)) {
            sessions.add(clientSessions.get(id));
        }
        return sessions;
    }

    @Override
    public List<WebSocketSession> getAllSessions() {
        return new ArrayList<>(clientSessions.values());
    }

    @Override
    public Set<String> getAllClientIds() {
        return new HashSet<>(clientSessions.keySet());
    }

    @Override
    public boolean connect(String id, WebSocketSession session) {
        if (!clientSessions.containsKey(id)) {
            clientSessions.put(id, session);
        }
        return true;
    }


    @Override
    public boolean disconnect(WebSocketSession session) {
        if (clientSessions.containsValue(session)) {
            Iterator<Map.Entry<String, WebSocketSession>> iterator = clientSessions.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, WebSocketSession> entry = iterator.next();
                if (entry.getValue().getId().equals(session.getId())) {
                    removeClientSession(entry.getKey());
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public boolean disconnect(String id) {
        if (clientSessions.containsKey(id)) {
            removeClientSession(id);
        }
        return true;
    }

    @Override
    public void clearAll() {
        Iterator<WebSocketSession> iterator = clientSessions.values().iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clientSessions.clear();
    }

    private boolean removeClientSession(String id) {
        WebSocketSession session = clientSessions.remove(id);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
