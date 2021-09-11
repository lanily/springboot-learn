package com.hsiao.springboot.kafka.high;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: DataHighwayConsumeGroup
 * @description: TODO
 * @author xiao
 * @create 2021/3/11
 * @since 1.0.0
 */
@Slf4j
public class DataHighwayConsumeGroup {

    private static final Duration POLL_TIME = Duration.of(200L, ChronoUnit.MILLIS);

    private final String groupId;
    private final Set<String> topics;
    private final List<Consumer> consumers;

    private final ExecutorService executorService;


    public DataHighwayConsumeGroup(String groupId, Set<String> topics,
            List<Consumer> consumers) {
        if (consumers == null || consumers.isEmpty()) {
            throw new IllegalArgumentException("At least one consumer should be provided.");
        }
        this.groupId = groupId;
        this.topics = topics;
        this.consumers = consumers == null ? Collections.emptyList() : consumers;
        this.executorService = Executors.newFixedThreadPool(consumers.size());
    }

    public <T> void consume(java.util.function.Consumer<T> messageProcessor,
            Supplier<Boolean> keepConsuming) {
        log.info("ConsumerGroup[{}] start consuming topics: {}", groupId, topics);
        consumers.forEach(consumer -> executorService.submit(() -> {
            consumer.subscribe(topics);
            try {
                while (keepConsuming.get()) {
                    ConsumerRecords<String, T> records = consumer.poll(POLL_TIME);
                    for (ConsumerRecord<String, T> record : records) {
                        T value = record.value();
                        log.info("Received message: topic={}, partition={}, offset={}, key={}",
                                record.topic(), record.partition(), record.offset(), record.key());
                        messageProcessor.accept(value);
                    }

                    if (!records.isEmpty()) {
                        doCommitSync(consumer);
                    }
                }
            } catch (Exception e) {
                // ignore for exception
            } finally {
                consumer.close();
            }
        }));
    }

    private void doCommitSync(Consumer consumer) {
        try {
            // Should be careful since wakeup() might be triggered while the commit is pending
            // The recursive call is safe since the wakeup will only be triggered once.
            consumer.commitSync();
        } catch (WakeupException e) {
            doCommitSync(consumer);
            throw e;
        } catch (Exception e) {
            // the commit  failed with an unrecoverable error. if there is any
            // internal state which depended on the commit, you can clean it
            // up here. otherwise it's reasonable to ignore the error and go on
            log.warn(e.getMessage());
        }
    }

    public void close() {
        log.info("Closing ConsumerGroup: {}", groupId);
        consumers.forEach(consumer -> {
            try {
// call consumer.wakeup() to interrupt an active poll, causing it to throw a WakeupException
                consumer.wakeup();
            } catch (Exception e) {
                e.printStackTrace();
                // can be ignored
            }
        });

        log.info("Closed ConsumeGroup: {}", groupId);
        executorService.shutdown();

        try {
            executorService.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // ignore
        }
    }

    @Override
    public String toString() {
        return "DataHighwayConsumeGroup{" +
                "groupId='" + groupId + '\'' +
                ", topics=" + topics +
                ", groupSize=" + consumers.size() +
                '}';
    }
}

