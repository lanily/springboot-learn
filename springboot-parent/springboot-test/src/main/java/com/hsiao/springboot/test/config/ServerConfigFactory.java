package com.hsiao.springboot.test.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ServerConfigFactory
 * @description: TODO
 * @author xiao
 * @create 2021/4/22
 * @since 1.0.0
 */
@Configuration
public class ServerConfigFactory {

    @Bean(name = "default_bean")
    @ConfigurationProperties(prefix = "server.default")
    public ServerConfig getDefaultConfigs() {
        return new ServerConfig();
    }
}

