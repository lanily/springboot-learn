package com.hsiao.springboot.websocket.dist.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsiao.springboot.websocket.dist.vo.MessageRequest;
import com.hsiao.springboot.websocket.dist.vo.MessageResponse;
import java.util.logging.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class MessageHandler extends TextWebSocketHandler {
    private final String charset = "UTF-8";
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    Logger logger = Logger.getLogger(MessageHandler.class.getName());

    MessageContext messageContext;

    public MessageHandler(MessageContext messageContext) {
        this.messageContext = messageContext;
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // TODO Auto-generated method stub
        String payload = message.getPayload();
        logger.info("handle text ===> " + payload);
        MessageResponse response;
        try {
            MessageRequest request = JSON_MAPPER
                    .readValue(payload, MessageRequest.class);
            response = this.messageContext.getMessageDispatcher().dispatch(request, session);
        } catch (Exception e) {
            e.printStackTrace();
            response = MessageResponse.failure(e.getMessage());
        }
        session.sendMessage(
                new TextMessage(JSON_MAPPER.writeValueAsString(response).getBytes(charset)));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        super.afterConnectionClosed(session, status);
        this.messageContext.getSessionManager().disconnect(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
            throws Exception {
        super.handleTransportError(session, exception);
        this.messageContext.getSessionManager().disconnect(session);
    }

}
