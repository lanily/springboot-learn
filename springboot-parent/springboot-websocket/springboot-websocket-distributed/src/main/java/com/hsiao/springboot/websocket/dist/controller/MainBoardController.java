package com.hsiao.springboot.websocket.dist.controller;

import com.hsiao.springboot.websocket.dist.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息面板
 *
 */
@RestController
@RequestMapping("/board")
public class MainBoardController {

    @Autowired
    SessionManager sessionManager;

    @RequestMapping("/listClients")
    public Object index() {
        StringBuilder result = new StringBuilder();
        for (String key : sessionManager.getAllClientIds()) {
            result.append(
                    "userId:" + key + "   cnt:" + sessionManager.getSession(key).size() + "<br>");
        }
        if (result.length() <= 0) {
            return "no user.";
        }
        return result.toString();
    }

}
