package com.hsiao.springboot.websocket.api.config;


import com.hsiao.springboot.websocket.api.handler.EchoWebSocketHandler;
import com.hsiao.springboot.websocket.api.handler.HttpAuthHandler;
import com.hsiao.springboot.websocket.api.handler.HttpHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 *
 * 使用Spring提供的低层级WebSocket API实现
 *
 * Spring 4.0为WebSocket通信提供了支持，包括：
 *
 * 发送和接收消息的低层级API；
 *
 * 发送和接收消息的高级API；
 *
 * 用来发送消息的模板；
 *
 * 支持SockJS，用来解决浏览器端、服务器以及代理不支持WebSocket的问题。
 *
 * 使用Spring提供的低层级API实现WebSocket，主要需要以下几个步骤：
 *
 * 说明
 * 通过实现WebSocketConfigurer 类并覆盖相应的方法进行 websocket 的配置。
 * 我们主要覆盖registerWebSocketHandlers 这个方法。通过向WebSocketHandlerRegistry 设置不同参数来进行配置。
 * 其中 addHandler 方法添加我们上面的写的 ws 的 handler 处理类，第二个参数是你暴露出的 ws 路径。
 * addInterceptors 添加我们写的握手过滤器。setAllowedOrigins("*") 这个是关闭跨域校验，
 * 方便本地调试，线上推荐打开。
 *
 * 链接：https://juejin.cn/post/6844903976727494669
 *
 * @projectName springboot-parent
 * @title: WebSocketConfig
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private HttpAuthHandler httpAuthHandler;

    @Autowired
    private HttpHandshakeInterceptor httpHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(httpAuthHandler, "/websocket")
                .addInterceptors(httpHandshakeInterceptor)
                .setAllowedOrigins("*");

//        registry
//                .addHandler(httpAuthHandler, "/sockjs")
//                .addInterceptors(httpHandshakeInterceptor)
//                .setAllowedOrigins("*");
//        registry.addHandler(echoWebSocketHandler(), "/websocket")
//                .setAllowedOrigins("*");

        registry.addHandler(echoWebSocketHandler(), "/sockjs")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Bean
    public WebSocketHandler echoWebSocketHandler() {
        return new EchoWebSocketHandler();
    }
}
