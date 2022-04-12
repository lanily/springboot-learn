package com.hsiao.springboot.websocket.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: WebSocketH5Config
 * @description: TODO
 * @author xiao
 * @create 2022/4/25
 * @since 1.0.0
 */
@Configuration
public class WebSocketH5Config {
  /**
   * ServerEndpointExporter 作用
   *
   * <p>这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
   *
   * @return
   */
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}
