package com.hsiao.springboot.websocket.h5.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *
 * 开启WebSocket支持
 *
 * 使用Java提供的@ServerEndpoint注解实现
 * @projectName springboot-parent
 * @title: WebSocketConfig
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
