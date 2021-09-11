package com.hsiao.springboot.kafka.avro;


import java.util.Properties;
import java.util.Random;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * 使用Confluent实现的Schema Registry服务来发送Avro序列化后的对象
 *
 * @projectName springboot-parent
 * @title: ConfluentProducer
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class ConfluentProducer {

    public static final String USER_SCHEMA = "{\"type\": \"record\", \"name\": \"User\", " +
            "\"fields\": [{\"name\": \"id\", \"type\": \"int\"}, " +
            "{\"name\": \"name\",  \"type\": \"string\"}, {\"name\": \"age\", \"type\": \"int\"}]}";

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.42.89:9092,192.168.42.89:9093,192.168.42.89:9094");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 使用Confluent实现的KafkaAvroSerializer
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        // 添加schema服务的地址，用于获取schema
        props.put("schema.registry.url", "http://192.168.42.89:8081");

        Producer<String, GenericRecord> producer = new KafkaProducer<>(props);

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(USER_SCHEMA);

        Random rand = new Random();
        int id = 0;

        while(id < 100) {
            id++;
            String name = "name" + id;
            int age = rand.nextInt(40) + 1;
            GenericRecord user = new GenericData.Record(schema);
            user.put("id", id);
            user.put("name", name);
            user.put("age", age);

            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("dev3-yangyunhe-topic001", user);

            producer.send(record);
            Thread.sleep(1000);
        }

        producer.close();

    }

}

