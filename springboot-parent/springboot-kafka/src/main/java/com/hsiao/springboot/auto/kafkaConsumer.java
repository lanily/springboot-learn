package com.hsiao.springboot.auto;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
/**
 *
 * 监听Kafka Topic, 从里面取数据
 *
 * @projectName springboot-parent
 * @title: kaConsumer
 * @description: TODO
 * @author xiao
 * @create 2021/2/1
 * @since 1.0.0
 */
@Service
public class kafkaConsumer {
    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * 第一种场景：监听一个Kafka 主题
     * **/
    @KafkaListener(topics = "${my.kafka.topic-name}")
    public void receiveMessageFromKafka(@Payload byte[] message, @Headers MessageHeaders headers) {
        logger.info("receive a message from kafka server start");

        String receiveMessage = new String(message);
        logger.debug("\r\n receive a message from kafka server:{}", receiveMessage);
        // 打印headers
        headers.keySet().forEach(key -> logger.debug("{}: {}", key, headers.get(key)));

        logger.info("receive a message from kafka server end");
    }

    /**
     * 第二种场景：监听一个Kafka 正则表达式
     * */
//	@KafkaListener(topicPattern = "${com.xingyun.kafka.topic-name}")
//	public void receiveMessageFromKafkaTopic(@Payload byte[] message, @Headers MessageHeaders headers) {
//		logger.info("receive a message from kafka server start");
//
//		String receiveMessage = new String(message);
//		logger.debug("\r\n receive a message from kafka server:{}", receiveMessage);
//		// 打印headers
//		headers.keySet().forEach(key -> logger.debug("{}: {}", key, headers.get(key)));
//
//		logger.info("receive a message from kafka server end");
//	}
}
