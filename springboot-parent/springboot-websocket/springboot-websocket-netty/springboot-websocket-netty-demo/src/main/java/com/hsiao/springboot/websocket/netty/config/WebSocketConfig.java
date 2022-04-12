package com.hsiao.springboot.websocket.netty.config;


import com.hsiao.springboot.websocket.netty.annotation.EnableWebSocket;
import com.hsiao.springboot.websocket.netty.server.ServerEndpointExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 说明：
 * 注意这里的@EnableWebSocket不是必须的，可以不用添加。
 *
 * @projectName springboot-parent
 * @title: WebSocketConfig
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig {

    /**
     * 用于扫描和注册所有携带ServerEndPoint注解的实例。
     * <p>
     * PS:若部署到外部容器 则无需提供此类。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
