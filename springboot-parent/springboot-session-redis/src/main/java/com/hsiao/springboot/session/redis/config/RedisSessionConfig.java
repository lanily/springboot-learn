/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: SessionConfig Author:   xiao Date:     2020/3/29 1:49
 * 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.session.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Redis session 配置类 在分布式项目中，为了保持无状态的应用，用redis做session服务器。 spring session可以和redis很好的配合
 *
 * 2.新建RedisSessionConfig类增加@EnableRedisHttpSession注解,
 * 开启spring boot对 springsession的支持并存储到redis中
 *
 * @projectName springboot-learn
 * @title: SessionConfig
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@Configuration
// 开启spring session支持
@EnableRedisHttpSession
// maxInactiveIntervalInSeconds设置session超时时间,默认1800秒
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {
    // spring boot项目可以直接配置application配置文件自动注册redis connect 工厂
    // 非boot项目则需要再这里配置一下redis得链接工厂
    // 注：如果不修改session超时，可以不用该配置类。

        @Bean
        public LettuceConnectionFactory connectionFactory() {
            return new LettuceConnectionFactory();
        }

    /**
     * redis 默认连接工厂
     * 以下作用
     * 1.作为spring session 分布式共享的Nosql数据源
     * 2.作为系统缓存/key值监听等缓存功能RedisTemplate的链接工厂
     * @return
     */
    /*@Bean
    public JedisConnectionFactory connectionFactory() {

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPoolConfig(new JedisPoolConfig());
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setPassword("password");
        jedisConnectionFactory.setDatabase(0);
        return jedisConnectionFactory;
    }*/
}
