package com.hsiao.springboot.websocket.stomp.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * 〈一句话功能简述〉<br>
 * https://github.com/aliakh/demo-spring-websocket
 * @projectName springboot-parent
 * @title: IndexController
 * @description: TODO
 * @author xiao
 * @create 2022/4/20
 * @since 1.0.0
 */
@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @MessageMapping("/request-without-response")
    public void handleMessageWithoutResponse(String message) {
        logger.info("Message without response: {}", message);
    }

    // response is sent to the endpoint /topic/request-with-implicit-response
    @MessageMapping("/request-with-implicit-response")
    public String handleMessageWithImplicitResponse(String message) {
        logger.info("Message with response: {}", message);
        return "response to " + HtmlUtils.htmlEscape(message);
    }

    @MessageMapping("/request")
    @SendTo("/queue/responses")
    public String handleMessageWithExplicitResponse(String message) {
        logger.info("Message with response: {}", message);
        if (message.equals("zero")) {
            throw new RuntimeException(String.format("'%s' is rejected", message));
        }
        return "response to " + HtmlUtils.htmlEscape(message);
    }

    @MessageExceptionHandler
    @SendTo("/queue/errors")
    public String handleException(Throwable exception) {
        logger.error("Server exception", exception);
        return "server exception: " + exception.getMessage();
    }
}
