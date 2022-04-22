package com.hsiao.springboot.websocket.api.controller;


import com.hsiao.springboot.websocket.api.handler.HttpAuthHandler;
import com.hsiao.springboot.websocket.api.handler.SessionManager;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: WebSocketController
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Controller
public class WebSocketController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpAuthHandler httpAuthHandler;


    @RequestMapping("/token")
    @ResponseBody
    public Object sendMsg(String token, String msg) throws IOException {
        WebSocketSession webSocketSession = SessionManager.get(token);
        if (webSocketSession == null) {
            return "用户登录已失效";
        }
        webSocketSession.sendMessage(new TextMessage(msg));
        return "消息发送成功";
    }

    @RequestMapping("/send")
    @ResponseBody
    public String send(HttpServletRequest request) {
        String username = request.getParameter("username");
        httpAuthHandler.sendToUser(username, new TextMessage("你好，测试！！！！"));
        return "websocket.html";
    }


    @RequestMapping("/broad")
    @ResponseBody
    public String broad() {
        httpAuthHandler.sendToAll(new TextMessage("发送一条小Broad"));
        logger.info("群发成功");
        return "success";
    }
}
