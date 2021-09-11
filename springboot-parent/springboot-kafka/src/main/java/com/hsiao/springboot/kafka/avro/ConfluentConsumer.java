package com.hsiao.springboot.kafka.avro;


import java.util.Collections;
import java.util.Properties;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * 使用Confluent实现的Schema Registry服务来消费Avro序列化后的对象
 *
 * @projectName springboot-parent
 * @title: ConfluentConsumer
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class ConfluentConsumer {

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.42.89:9092,192.168.42.89:9093,192.168.42.89:9094");
        props.put("group.id", "dev3-yangyunhe-group001");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 使用Confluent实现的KafkaAvroDeserializer
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        // 添加schema服务的地址，用于获取schema
        props.put("schema.registry.url", "http://192.168.42.89:8081");
        KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("dev3-yangyunhe-topic001"));

        try {
            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(1000);
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    GenericRecord user = record.value();
                    System.out
                            .println("value = [user.id = " + user.get("id") + ", " + "user.name = "
                                    + user.get("name") + ", " + "user.age = " + user.get("age")
                                    + "], "
                                    + "partition = " + record.partition() + ", " + "offset = "
                                    + record.offset());
                }
            }
        } finally {
            consumer.close();
        }
    }
}


