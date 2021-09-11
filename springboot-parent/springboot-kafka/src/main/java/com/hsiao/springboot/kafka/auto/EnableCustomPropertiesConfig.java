package com.hsiao.springboot.kafka.auto;


/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EnableCustomPropertiesConfig
 * @description: TODO
 * @author xiao
 * @create 2021/2/1
 * @since 1.0.0
 */

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置自定义Properties属性
 * */
@EnableConfigurationProperties({
        KafkaTopicCustomProperties.class
})
@Configuration
public class EnableCustomPropertiesConfig {
    /**
     *
     * 静态方法是没有办法调用这个配置文件内容的
     *
     */
}
