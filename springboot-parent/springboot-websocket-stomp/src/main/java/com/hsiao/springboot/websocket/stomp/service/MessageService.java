package com.hsiao.springboot.websocket.stomp.service;


import com.hsiao.springboot.websocket.stomp.constants.WsConstants.Broker;
import com.hsiao.springboot.websocket.stomp.model.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: MessageService
 * @description: TODO
 * @author xiao
 * @create 2022/4/20
 * @since 1.0.0
 */
@Service
public class MessageService {


    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageService(
            SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendToUser(MessageRequest messageRequest) {
        // convertAndSendToUser 方法可以发送给指定用户
        // 底层会自动将第二个参数目的地址：/queue/alone拼接为
        // /user/username/queue/alone，其中第二个参数username 即为这里的第一个参数
        // username 也是前文中配置的Principal 用户识别标志
        // 使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为"user/" + 用户id + "/queue"，其中"user"是固定的

        simpMessagingTemplate.convertAndSendToUser(
//                "admin",
                String.valueOf(messageRequest.getTo()),
                Broker.BROKER_QUEUE + "alone",
                messageRequest.getContent());

    }
}
