package com.hsiao.springboot.websocket.chat.controller;

import com.hsiao.springboot.websocket.chat.config.WebSocketServer;
import com.hsiao.springboot.websocket.chat.model.ChatMessage;
import com.hsiao.springboot.websocket.chat.model.User;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

  @MessageMapping("/chat.register")
  @SendTo("/topic/public")
  public ChatMessage register(
      @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    // Add username in web socket session
    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    return chatMessage;
  }

  @MessageMapping("/chat.send")
  @SendTo("/topic/public")
  public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    return chatMessage;
  }

  @RequestMapping("/users")
  @ResponseBody
  public Set<String> users(@RequestParam("username") String username) {
    ConcurrentHashMap<String, Session> map = WebSocketServer.getSessionPools();
    Set<String> set = map.keySet();
    Iterator<String> it = set.iterator();
    Set<String> users = new HashSet<>();
    while (it.hasNext()) {
      String entry = it.next();
      if (!entry.equals(username)) {
        users.add(entry);
      }
    }
    return users;
  }

  @RequestMapping("id")
  @ResponseBody
  public User getUser(@RequestParam("username") String username) {
    User u = new User();
    u.setId(new Random().nextInt(10));
    u.setName(username);
    return u;
  }
}
