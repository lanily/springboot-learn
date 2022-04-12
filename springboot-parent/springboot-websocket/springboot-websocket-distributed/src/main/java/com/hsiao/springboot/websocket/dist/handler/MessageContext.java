package com.hsiao.springboot.websocket.dist.handler;

import com.hsiao.springboot.websocket.dist.config.MessageConfig;
import com.hsiao.springboot.websocket.dist.session.SessionManager;

public class MessageContext {

    MessageConfig messageConfig;

    SessionManager sessionManager;

    MessageDispatcher messageDispatcher;

    MessagePoster messagePoster;

    public MessageContext(MessageConfig messageConfig, SessionManager sessionManager,
            MessageDispatcher messageDispatcher,
            MessagePoster messagePoster) {
        this.messageConfig = messageConfig;
        this.sessionManager = sessionManager;
        this.messageDispatcher = messageDispatcher;
        this.messagePoster = messagePoster;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public MessagePoster getMessagePoster() {
        return messagePoster;
    }
}
