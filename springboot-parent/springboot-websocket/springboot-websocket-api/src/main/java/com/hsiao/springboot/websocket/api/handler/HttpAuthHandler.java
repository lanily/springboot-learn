package com.hsiao.springboot.websocket.api.handler;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * 说明
 * 通过继承 TextWebSocketHandler 类并覆盖相应方法，可以对 websocket 的事件进行处理，这里可以同原生注解的那几个注解连起来看
 *
 * afterConnectionEstablished 方法是在 socket 连接成功后被触发，同原生注解里的 @OnOpen 功能
 * afterConnectionClosed 方法是在 socket 连接关闭后被触发，同原生注解里的 @OnClose 功能
 * handleTextMessage 方法是在客户端发送信息时触发，同原生注解里的@OnMessage 功能
 *
 * 重写TextWebSocketHandler的四个方法：
 *
 * afterConnectionEstablished 成功创建连接后调用
 *
 * handleTextMessage 收到客户端消息后调用
 *
 * handleTransportError 连接异常时调用
 *
 * afterConnectionClosed 连接关闭后调用
 *
 * WebSocketSession是客户端与服务端建立的回话，可以通过close()方法主动关闭连接
 *
 * TextMessage为收到的消息，可以通过getPayload()方法获取消息内容
 * @projectName springboot-parent
 * @title: HttpAuthHandler
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Component
public class HttpAuthHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpAuthHandler.class);

    //用户标识, 对应监听器从的key
    private static final String USER_ID = "WEBSOCKET_USERID";

    /**
     * socket 建立成功事件
     * 连接成功时候，会触发页面上onopen方法
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object token = session.getAttributes().get("token");
        String userId = (String) session.getAttributes().get(USER_ID);
        if (token != null) {
            // 用户连接成功，放入在线用户缓存
            SessionManager.add(token.toString(), session);
        } else if (StringUtils.isNotBlank(userId)) {
            SessionManager.add(userId, session);
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 接收消息事件
     * js调用websocket.send时候，会调用该方法
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Object token = session.getAttributes().get("token");
        logger.info("server 接收到 {} 发送的 {}", token, payload);

        if (Objects.nonNull(token)) {
            sendToUser((String) token,
                    new TextMessage("server 发送给 " + token + " 消息 " + payload + " " + LocalDateTime
                            .now().toString()));
        }
//        session.sendMessage(
//                new TextMessage("server 发送给 " + token + " 消息 " + payload + " " + LocalDateTime
//                        .now().toString()));
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        logger.debug("传输出现异常，关闭websocket连接... ");
        String userId= (String) session.getAttributes().get(USER_ID);
        SessionManager.remove(userId);
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        Object token = session.getAttributes().get("token");
        String userId= (String) session.getAttributes().get(USER_ID);
        if (token != null) {
            // 用户退出，移除缓存
            SessionManager.remove(token.toString());
        } else if (StringUtils.isNotBlank(userId)) {
            SessionManager.remove(userId);
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     */
    public void sendToUser(String userId, TextMessage message) {
        ConcurrentHashMap<String, WebSocketSession> users = SessionManager.getSessionMaps();
        for (String id : users.keySet()) {
            if (id.equals(userId)) {
                try {
                    if (users.get(id).isOpen()) {
                        users.get(id).sendMessage(message);
                    }
                } catch (IOException e) {
                    logger.error("failed to send user:{} with message:{}", userId,
                            message.getPayload(), e);
                }
                break;
            }
        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendToAll(TextMessage message) {
        ConcurrentHashMap<String, WebSocketSession> users = SessionManager.getSessionMaps();
        for (String userId : users.keySet()) {
            try {
                if (users.get(userId).isOpen()) {
                    users.get(userId).sendMessage(message);
                }
            } catch (IOException e) {
                logger.error("failed to send all users with message:{}",
                        message.getPayload(), e);
            }
        }
    }

}
