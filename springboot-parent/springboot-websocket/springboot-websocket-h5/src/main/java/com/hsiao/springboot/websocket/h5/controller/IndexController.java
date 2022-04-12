package com.hsiao.springboot.websocket.h5.controller;


import com.hsiao.springboot.websocket.h5.server.WebSocketServer;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: IndexController
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
public class IndexController {

    @GetMapping("index")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("请求成功");
    }

    @GetMapping("page")
    public ModelAndView page() {
        return new ModelAndView("websocket");
    }

    @RequestMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId)
            throws IOException {
        WebSocketServer.sendToUser(message, toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }
}
