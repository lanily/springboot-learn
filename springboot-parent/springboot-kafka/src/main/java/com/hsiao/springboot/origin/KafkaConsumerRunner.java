package com.hsiao.springboot.origin;


import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: KafkaConsumerRunner
 * @description: TODO
 * @author xiao
 * @create 2021/2/1
 * @since 1.0.0
 */
public class KafkaConsumerRunner implements Runnable {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaConsumer consumer;

    public KafkaConsumerRunner(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(Arrays.asList("topic"));
            while (!closed.get()) {
                // 执行消息处理逻辑
                ConsumerRecords records = consumer.poll(10000);
            }
        } catch (Exception e) {
            // Ignore exception if closing
            if (!closed.get()) {
                throw e;
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * Shutdown hook which can be called from a separate thread
     */
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}

