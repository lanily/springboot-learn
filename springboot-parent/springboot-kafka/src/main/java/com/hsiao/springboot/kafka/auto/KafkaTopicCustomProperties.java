package com.hsiao.springboot.kafka.auto;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: KafkaTopicCustomProperties
 * @description: TODO
 * @author xiao
 * @create 2021/2/1
 * @since 1.0.0
 */
//自定义前缀
@ConfigurationProperties(prefix="my.kafka")
public class KafkaTopicCustomProperties {
    // 这种命令格式在xml中就是my.kafka.topic-name属性值
    private String topicName;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
