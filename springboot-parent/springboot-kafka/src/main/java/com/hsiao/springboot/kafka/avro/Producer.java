package com.hsiao.springboot.kafka.avro;


import java.util.Properties;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Producer
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class Producer<T extends SpecificRecordBase> {

    private KafkaProducer<String, T> producer = new KafkaProducer<>(getProperties());

    public void sendData(Topic topic, T data) {
        producer.send(new ProducerRecord<>(topic.topicName, data),
                (metadata, exception) -> {
                    if (exception == null) {
                        System.out.printf("Sent user: %s \n", data);
                    } else {
                        System.out.println("data sent failed: " + exception.getMessage());
                    }
                });
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                AvroSerializer.class.getName());
        return props;
    }
}
