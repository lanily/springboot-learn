package com.hsiao.springboot.websocket.dist.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsiao.springboot.websocket.dist.session.WebSocketSessionAdapter;
import com.hsiao.springboot.websocket.dist.vo.MessageRequest;
import com.hsiao.springboot.websocket.dist.vo.MessageResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.socket.WebSocketSession;


public class MessageDispatcher {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    DispatcherServlet dispatcherServlet;

    public MessageDispatcher(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }


    /**
     * 接口分发
     * @param req
     * @return
     */
    public MessageResponse dispatch(MessageRequest req, WebSocketSession webSocketSession) {

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest(req.getMethod(), req.getUri());
        try {

            Map parameter = req.getData();
            if (parameter == null) {
                parameter = new HashMap();
            }

            if ("form".equals(req.getType())) {
                request.addParameters(parameter);
            } else if ("content".equals(req.getType())) {
                request.setContent(JSON_MAPPER.writeValueAsBytes(parameter));
                request.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            } else {
                return MessageResponse.failure("error type : " + req.getType());
            }

            request.setSession(new WebSocketSessionAdapter(webSocketSession));
//          request.getSession(true).setAttribute(SessionManager.SESSION_KEY, webSocketSession);
            dispatcherServlet.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.failure("dispatch error with msg : " + e.getMessage());
        }

        try {

            MessageResponse messageResponse = MessageResponse
                    .success();
            String content = response.getContentAsString();
            if (!StringUtils.isEmpty(content)) {
                if (content.startsWith("{") || content.startsWith("[")) {
                    Map result = JSON_MAPPER.readValue(content, Map.class);
                    messageResponse.put("data", result);
                } else {
                    messageResponse.put("data", content);
                }
            }
            return messageResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.failure("response error with msg : " + e.getMessage());
        }

    }

}
