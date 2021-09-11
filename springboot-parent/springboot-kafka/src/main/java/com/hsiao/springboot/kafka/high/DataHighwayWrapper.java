package com.hsiao.springboot.kafka.high;


import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: DataHighwayWrapper
 * @description: TODO
 * @author xiao
 * @create 2021/3/10
 * @since 1.0.0
 */
public class DataHighwayWrapper {

    private final DataHighwayConfiguration config;
    private final Set<String> topics;

    public DataHighwayWrapper(DataHighwayConfiguration config,
            String topic) {
        this.config = config;
        this.topics = Collections.singleton(topic);
    }

    public DataHighwayWrapper(DataHighwayConfiguration config,
            Set<String> topics) {
        this.config = config;
        this.topics = topics;
    }

    public DataHighwayProducer createDHProducer(String clientId) {
        return createDHProducer(true, clientId);
    }

    public DataHighwayProducer createDHProducer(boolean useSchemaRegistry, String clientId) {
        Producer producer = createProducer(useSchemaRegistry, clientId);
        return new DataHighwayProducer(producer, topics);
    }

    public Producer createProducer(String clientId) {
        return createProducer(true, clientId);
    }

    public DataHighwayConsumeGroup createDHConsumerGroup(String groupId, int groupSize) {
        return createDHConsumerGroup(true, groupId, groupSize);
    }

    public DataHighwayConsumeGroup createDHConsumerGroup(boolean useSchemaRegistry, String groupId,
            int groupSize) {
        List<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < groupSize; i++) {
            consumers.add(createConsumer(useSchemaRegistry, groupId));
        }
        return new DataHighwayConsumeGroup(groupId, topics, consumers);
    }

    private Consumer createConsumer(boolean useSchemaRegistry, String groupId) {
        final Properties props = configProducer(useSchemaRegistry, groupId);
        Consumer consumer = new KafkaConsumer(props);
        return consumer;
    }

    public Producer createProducer(boolean useSchemaRegistry, String clientId) {
        final Properties props = configProducer(useSchemaRegistry, clientId);
        Producer producer = new KafkaProducer(props);
        return producer;
    }

    private Properties configConsumer(boolean useSchemaRegistry, String groupId) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
                RoundRobinAssignor.class.getName());

        if (useSchemaRegistry) {
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    KafkaAvroDeserializer.class.getName());
            props.put("schema.registry.url", config.getSchemaRegistryUrl());
            props.put("specific.avro.reader", "true");
        } else {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class.getName());
        }
        enableSsl(props);
        return props;
    }

    private Properties configProducer(boolean useSchemaRegistry, String clientId) {
        final Properties props = new Properties();
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootStrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (useSchemaRegistry) {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    KafkaAvroSerializer.class.getName());
            props.put("schema.registry.url", config.getSchemaRegistryUrl());
        } else {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class.getName());
        }
        enableSsl(props);
        return props;
    }

    private void enableSsl(Properties props) {
        String trustStoreLocation = config.isTrustStoreInClasspath() ?
                DataHighwayWrapper.class.getClassLoader().getResource(config.getTrustStorePath())
                        .getPath() : config.getTrustStorePath();
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, config.getTrustStoreKey());
    }


}

