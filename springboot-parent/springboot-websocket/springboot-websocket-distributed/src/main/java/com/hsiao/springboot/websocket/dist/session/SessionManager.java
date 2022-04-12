package com.hsiao.springboot.websocket.dist.session;


import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;


public interface SessionManager {

    String SESSION_KEY = "WS_SESSION";

    List<WebSocketSession> getSession(String clientId);

    List<WebSocketSession> getAllSessions();

    Set<String> getAllClientIds();

    boolean connect(String id, WebSocketSession session);

    boolean disconnect(WebSocketSession session);

    boolean disconnect(String clientId);

    void clearAll();
}
