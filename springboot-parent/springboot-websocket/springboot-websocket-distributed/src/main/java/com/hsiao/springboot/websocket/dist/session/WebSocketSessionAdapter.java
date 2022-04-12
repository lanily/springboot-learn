package com.hsiao.springboot.websocket.dist.session;

import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketSessionAdapter implements HttpSession {

    WebSocketSession session;

    long createionTime;

    public WebSocketSessionAdapter(WebSocketSession session) {
        this.session = session;
        createionTime = System.currentTimeMillis();
    }

    public WebSocketSession getWebSocketSession() {
        return session;
    }

    @Override
    public long getCreationTime() {
        return createionTime;
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return session.getAttributes().get(s);
    }

    @Override
    public Object getValue(String s) {
        return session.getAttributes().get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new Vector<String>(session.getAttributes().keySet()).elements();
    }

    @Override
    public String[] getValueNames() {
        return (String[]) session.getAttributes().values().toArray();
    }

    @Override
    public void setAttribute(String s, Object o) {
        session.getAttributes().put(s, o);
    }

    @Override
    public void putValue(String s, Object o) {
        session.getAttributes().put(s, o);
    }

    @Override
    public void removeAttribute(String s) {
        session.getAttributes().remove(s);
    }

    @Override
    public void removeValue(String s) {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }
}
