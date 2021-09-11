package com.hsiao.springboot.kafka.avro;


import com.hsiao.springboot.kafka.model.User;
import java.util.EnumSet;
import org.apache.avro.specific.SpecificRecordBase;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Topic
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public enum Topic {
    USER("user-info-topic", new User());

    public final String topicName;
    public final SpecificRecordBase topicType;

    Topic(String topicName, SpecificRecordBase topicType) {
        this.topicName = topicName;
        this.topicType = topicType;
    }

    public static Topic matchFor(String topicName) {
        return EnumSet.allOf(Topic.class).stream()
                .filter(topic -> topic.topicName.equals(topicName))
                .findFirst()
                .orElse(null);
    }
}

