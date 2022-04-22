package com.hsiao.springboot.websocket.stomp.controller;


import com.hsiao.springboot.websocket.stomp.model.MessageRequest;
import com.hsiao.springboot.websocket.stomp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: MessageController
 * @description: TODO
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
@RestController
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

//    @ResponseBody
    @PostMapping("/pushToOne")
//    @RequestMapping("pushToOne")
    public ResponseEntity<Object> sendToUserWithRestMethod(@RequestBody MessageRequest messageRequest) {
        logger.info("pushToOne received message : {}", messageRequest.toString());
        messageService.sendToUser(messageRequest);
        return ResponseEntity.ok().build();
    }
}
