package com.hsiao.springboot.websocket.dist.config;

import com.hsiao.springboot.websocket.dist.handler.MessageHandler;
import javax.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置
 * Created by lb on 2018/1/2.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    public static final String MAPPING_URI = "uri";

    @Resource
    MessageConfig messageConfig;

    @Resource
    MessageHandler messageHandler;

    @Bean
    public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageHandler, messageConfig.getServerRoot()).addInterceptors(new com.wbs.message.core.WebSocketInterceptor()).setAllowedOrigins("*");
    }

}
