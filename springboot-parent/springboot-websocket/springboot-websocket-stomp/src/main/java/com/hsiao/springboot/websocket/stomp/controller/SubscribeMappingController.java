package com.hsiao.springboot.websocket.stomp.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * https://github.com/aliakh/demo-spring-websocket/tree/master/websocket-sockjs-stomp-server
 *
 * 先前端stompjs有两种方式向后端交互，一种是发送消息send，一种是订阅subscribe,它们在都会带一个目的地址/app/hello
 * 如果地址前缀是/app，那么此消息会关联到@MessageMapping(send命令会到这个注解)、@SubscribeMapping(subscribe命令会到这个注解)中，如果没有/app，则不会映射到任何注解上去，例如：
 *
 * @projectName springboot-parent
 * @title: SubscribeMappingController
 * @description: TODO
 * @author xiao
 * @create 2022/4/20
 * @since 1.0.0
 */
@Controller
public class SubscribeMappingController {

    private static final Logger logger = LoggerFactory.getLogger(SubscribeMappingController.class);

    @SubscribeMapping("/subscribe")
    public String sendOneTimeMessage() {
        logger.info("Subscription via the application");
        return "server one-time message via the application";
    }
}
