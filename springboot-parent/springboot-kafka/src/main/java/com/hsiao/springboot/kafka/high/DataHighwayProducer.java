package com.hsiao.springboot.kafka.high;


import java.util.Collections;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName flink-common
 * @title: DataHighwayProducer
 * @description: TODO
 * @author xiao
 * @create 2021/3/10
 * @since 1.0.0
 */
@Slf4j
public class DataHighwayProducer {

    private final Producer producer;
    private final Set<String> topics;

    public DataHighwayProducer(Producer producer, String topic) {
        this.producer = producer;
        this.topics = Collections.singleton(topic);
    }

    public DataHighwayProducer(Producer producer, Set<String> topics) {
        this.producer = producer;
        this.topics = topics;
    }

    public <V> void send(V message) {
        send(null, message);
    }


    public <K, V> void send(K key, V message) {
        topics.forEach(topic -> {
            ProducerRecord<K, V> record = new ProducerRecord<K, V>(topic, key, message);
            long time = System.currentTimeMillis();
            producer.send(record, (RecordMetadata metadata, Exception exception) -> {
                long elapsedTime = System.currentTimeMillis() - time;
                log.info(
                        "Kafka message sent to data high way, topic={}, value={}, meta(partition={}, offset={}) time={}",
                        topic, record.key(), record.value(), metadata.partition(),
                        metadata.offset(), elapsedTime);

                if (exception != null) {
                    throw new RuntimeException(exception);
                }
            });
            producer.flush();

        });
    }

    public void  close () {
        try {
            producer.close();
        } catch (Exception e) {
            // can be ignored
        }
    }
}

