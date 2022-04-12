package com.hsiao.springboot.websocket.dist.controller;


import com.hsiao.springboot.websocket.dist.handler.MessagePoster;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;


@RestController
@RequestMapping("/msg")
public class MessageController {


    @Autowired
    MessagePoster messagePoster;


    @RequestMapping("/test")
    public String countTicket() {
        System.out.println("msg----------");
        return "wonderful";
    }

    @RequestMapping("/hello")
    public String hello(Map map, HttpSession session) {
        System.out.println(session.getId());
        System.out.println("msg-----hello---------" + map);
        return "from hello";
    }


    @RequestMapping("/login")
    public String login(Map map, WebSocketSession session) {
        System.out.println("msg-----login---------" + map);
//      MessagePoster.instance().connect(map.get("uid"), session);
        return "from login";
    }


    @RequestMapping("/broadcast")
    public void broadcast() {
        System.out.println("msg-----broadcast---------");
        try {
            messagePoster.broadcast(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
