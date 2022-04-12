package com.hsiao.springboot.websocket.dist.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsiao.springboot.websocket.dist.session.SessionManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 消息发送
 */
public class MessagePoster {

    private final static String default_charset = "UTF-8";
    private final static String stamp_default = "stamp";
    private final static String stamp_broadcast = "broadcast";

    SessionManager sessionManager;

    public MessagePoster(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 广播，邮戳默认为broadcast
     */
    public void broadcast(Object parameter) throws IOException {
        sendToAll(parameter, stamp_broadcast);
    }

    public void sendMessage(WebSocketSession session, String msg) throws IOException {
        session.sendMessage(new TextMessage(msg.getBytes(default_charset)));
    }

    public void sendToOne(Object parameter, WebSocketSession session) throws IOException {
        sendToOne(parameter, stamp_default, session);
    }

    public void sendToOne(Object parameter, String stamp, WebSocketSession session)
            throws IOException {
        Map result = new HashMap();
        result.put("data", parameter);
        result.put("stamp", stamp);
        sendMessage(session, new ObjectMapper().writeValueAsString(result));
    }

    public void sendToOne(Object parameter, String stamp, String id) throws IOException {
        List<WebSocketSession> sessions = sessionManager.getSession(id);
        for (WebSocketSession tmp : sessions) {
            sendToOne(parameter, stamp, tmp);
        }
    }

    private void sendToAll(Object parameter, String stamp) throws IOException {
        List<WebSocketSession> sessions = sessionManager.getAllSessions();
        for (WebSocketSession session : sessions) {
            sendToOne(parameter, stamp, session);
        }
    }


}
