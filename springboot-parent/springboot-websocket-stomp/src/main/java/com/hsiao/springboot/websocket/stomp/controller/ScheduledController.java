package com.hsiao.springboot.websocket.stomp.controller;


import java.time.LocalTime;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ScheduledController
 * @description: TODO
 * @author xiao
 * @create 2022/4/20
 * @since 1.0.0
 */
@Component
public class ScheduledController {

    private final MessageSendingOperations<String> messageSendingOperations;

    public ScheduledController(MessageSendingOperations<String> messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    @Scheduled(fixedDelay = 10000)
    public void sendPeriodicMessages() {
        String broadcast = String.format("server periodic message %s via the broker", LocalTime.now());
        this.messageSendingOperations.convertAndSend("/topic/periodic", broadcast);
    }
}
