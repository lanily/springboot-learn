package com.hsiao.springboot.websocket.api.handler;


import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EchoWebSocketHandler
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
public class EchoWebSocketHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("Opened new session in instance " + this);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        //组装返回的Echo信息
        String echoMessage = message.getPayload();
        logger.info(MessageFormat.format("Echo message \"{0}\"", echoMessage));
        session.sendMessage(new TextMessage(echoMessage));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
            throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
        logger.debug("Info: WebSocket connection closed.");
    }
}
