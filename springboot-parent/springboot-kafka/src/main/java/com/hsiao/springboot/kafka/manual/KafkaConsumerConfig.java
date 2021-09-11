package com.hsiao.springboot.kafka.manual;


import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * https://blog.csdn.net/qq_26869339/article/details/88324980
 * 若调整session-timeout不起效果，请调整max-poll-interval参数，一条业务线没有跑完，时间已经超出了要poll的时间，就会报如上错误。
 * 若数据积压，可以调整max-partition-fetch-bytes参数，默认1M（110241024），只会拉取1M的数据，配合max-poll-records可以限制批量拉取数据的数量
 *
 * 总结：自动提交，在服务启停时，会有重复数据被生产到kafka中，保证吞吐量的同时，降低了kafka的原子性；手动提交，保证了kafka的原子性，同时降低了kafka的吞吐量，实际开发中，可跟随数据量的大小，自行分析配置。
 * @projectName springboot-parent
 * @title: KafkaConsumerConfig
 * @description: TODO
 * @author xiao
 * @create 2021/2/1
 * @since 1.0.0
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean autoCommit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private Integer autoCommitInterval;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private Integer maxPollRecords;

    @Value("${spring.kafka.consumer.max-poll-interval}")
    private Integer maxPollInterval;

    @Value("${spring.kafka.consumer.max-partition-fetch-bytes}")
    private Integer maxPartitionFetchBytes;

    @Value("${spring.kafka.consumer.session-timeout}")
    private String sessionTimeout;

    @Value("${spring.kafka.listener.concurrency}")
    private Integer concurrency;

    @Value("${spring.kafka.listener.poll-timeout}")
    private Long pollTimeout;

    @Value("${spring.kafka.listener.batch-listener}")
    private Boolean batchListener;

    @Value("#{'${spring.kafka.listener.concurrencys}'.split(',')[0]}")
    private Integer concurrency3;

    @Value("#{'${spring.kafka.listener.concurrencys}'.split(',')[1]}")
    private Integer concurrency6;
    /**
     * AckMode针对ENABLE_AUTO_COMMIT_CONFIG=false时生效，有以下几种：
     *
     * RECORD
     * 每处理一条commit一次
     *
     * BATCH(默认)
     * 每次poll的时候批量提交一次，频率取决于每次poll的调用频率
     *
     * TIME
     * 每次间隔ackTime的时间去commit(跟auto commit interval有什么区别呢？)
     *
     * COUNT
     * 累积达到ackCount次的ack去commit
     *
     * COUNT_TIME
     * ackTime或ackCount哪个条件先满足，就commit
     *
     * MANUAL
     * listener负责ack，但是背后也是批量上去
     *
     * MANUAL_IMMEDIATE
     * listner负责ack，每调用一次，就立即commit
     *
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // 多线程消费分区
        factory.setConcurrency(concurrency);
        // 批量消费
        factory.setBatchListener(batchListener);
        // 如果消息队列中没有消息，等待timeout毫秒后，调用poll()方法。
        // 如果队列中有消息，立即消费消息，每次消费的消息的多少可以通过max.poll.records配置。
        // 手动提交无需配置
        factory.getContainerProperties().setPollTimeout(pollTimeout);
        // 设置提交偏移量的方式，
//        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        // 配置手动提交offset
        factory.getContainerProperties().setAckMode((ContainerProperties.AckMode.MANUAL));
        return factory;
    }


    /**
     * 并发数6
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "kafkaBatchListener6")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaBatchListener6() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaListenerContainerFactory();
        factory.setConcurrency(concurrency6);
        return factory;
    }
    /**
     * 并发数3
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "kafkaBatchListener3")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaBatchListener3() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaListenerContainerFactory();
        factory.setConcurrency(concurrency3);
        return factory;
    }

    /**
     * 单线程-单条消费
     * @return
     */
   /* @Bean
    public KafkaListenerContainerFactory<?> singleFactory() {
        Map<String, Object> configProps = new HashMap<>(4);
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, topic);

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));

        return factory;
    }*/

    /**
     * 多线程-批量消费
     * @return
     */
    /*@Bean
    public KafkaListenerContainerFactory<?> batchFactory(){
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // 控制多线程消费
        // 并发数(如果topic有3各分区。设置成3，并发数就是3个线程，加快消费)
        // 不设置setConcurrency就会变成单线程配置, MAX_POLL_RECORDS_CONFIG也会失效，
        // 接收的消息列表也不会是ConsumerRecord
        factory.setConcurrency(10);
        // poll超时时间
        factory.getContainerProperties().setPollTimeout(1500);
        // 控制批量消费
        // 设置为批量消费，每个批次数量在Kafka配置参数中设置（max.poll.records）
        factory.setBatchListener(true);
        return factory;
    }*/

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(11);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    public ConsumerFactory<String,String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

}


