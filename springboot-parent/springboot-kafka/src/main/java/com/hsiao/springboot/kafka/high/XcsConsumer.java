package com.hsiao.springboot.kafka.high;


import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: XcsConsumer
 * @description: TODO
 * @author xiao
 * @create 2021/3/11
 * @since 1.0.0
 */
@Component
@Slf4j
public class XcsConsumer {

    private static final Duration POLL_TIME = Duration.of(1000L, ChronoUnit.MILLIS);
    private static final int MAX_NO_MESSAGES_FOUND_COUNT = 10;

    private final String schemaRegistryUrl;
    private final Consumer<String, GenericRecord> kafkaConsumer;
    private final RestTemplate restTemplate;
    private final NumberFormat amountFormat;

    public XcsConsumer(KafkaProperties kafkaProperties,
            Consumer<String, GenericRecord> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
        this.schemaRegistryUrl = kafkaProperties.getProperties().get("schema.registry.url");
        this.restTemplate = new RestTemplate();
        amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setMinimumFractionDigits(3);
        amountFormat.setMaximumFractionDigits(20);
        amountFormat.setGroupingUsed(false);
    }

    public void consume(String topic) {
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        // writer.println("Callable Reference,Scenario Reference, Amount");
        int noMessagesFoundCounter = 0;
        boolean runnable = true;
        try {
            while (runnable) {
                ConsumerRecords<String, GenericRecord> messages = kafkaConsumer.poll(POLL_TIME);
                if (0 == messages.count()) {
                    ++noMessagesFoundCounter;
                    if (noMessagesFoundCounter > MAX_NO_MESSAGES_FOUND_COUNT) {
                        // no record, consumer will leave group and close
                        runnable = false;
                    }
                }
                messages.forEach(m -> process(m));
                kafkaConsumer.commitAsync();
            }
        } catch (Exception e) {
            log.error("Failed commit offset with exception: " + e.getMessage());
            e.printStackTrace();
            // ignore for shutdown
            // System.exit(1);
        }
    }

    private void process(ConsumerRecord<String, GenericRecord> record) {
        Optional<?> message = Optional.ofNullable(record);
        try {
            if (message.isPresent()) {
                GenericRecord data = record.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
            doCommitSync(kafkaConsumer);
        }

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

    /**
     * Checks if topic of given name is present on Data Highway Validates compatibility of schema against one registered
     * in topic
     * @param topic
     */
    public void verify(String topic) {
        verifyTopicExistence(topic);
        verifyRegisteredSchema(topic);
    }

    private void verifyTopicExistence(String topic) {
        boolean topicFound = kafkaConsumer
                .listTopics()
                .values()
                .stream()
                .flatMap(List::stream)
                .anyMatch(topic::equals);

        if (!topicFound) {
            throw new IllegalArgumentException(
                    String.format("Did not find topic named \"%s\" on Data Highway.", topic));
        }
    }

    private void verifyRegisteredSchema(String topic) {
        try {
            JsonObject payloadSchema = new JsonObject();
//            payloadSchema.addProperty("schema", JobResult.SCHEMA$.toString());
            final String urlString = schemaRegistryUrl + "/compatibility/subjects/" + topic
                    + "-value/versions/latest";
            final MediaType mediaType = MediaType
                    .valueOf("application/vnd.schemaregistry.v1+json; charset=utf-8");
            RequestEntity<String> requestEntity = (RequestEntity<String>) RequestEntity
                    .post(new URL(urlString).toURI())
                    .contentType(mediaType)
                    .body(payloadSchema.toString());
            verifyResponse(restTemplate.exchange(requestEntity, Map.class));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            log.error("Schema registry URL exception: " + e.getMessage(), e);
            throw new IllegalArgumentException("Schema registry URL exception: " + e.getMessage(),
                    e);
        }
    }

    private void verifyResponse(ResponseEntity<Map> response) {
        String exceptionMessage;
        if (!response.hasBody()) {
            exceptionMessage =
                    "Unknown error. Schema registry returned empty response (code: " + response
                            .getStatusCode() + ").";

        } else {
            if (HttpStatus.OK.equals(response.getStatusCode())) {
                Map responseMap = response.getBody();
                if (responseMap.containsKey("is_compatible") && (boolean) responseMap
                        .get("is_compatible")) {
                    return;
                } else {
                    exceptionMessage = "Schema in use is not compatible with one registered in Data Highway.";
                }
            } else {
                exceptionMessage = "Schema registry returned error response with code: " + response
                        .getStatusCode() + "\nand body: " + response.getBody().toString();
            }
        }
        log.error("Schema verification failed. {}", exceptionMessage);
        throw new IllegalArgumentException("Schema verification failed." + exceptionMessage);
    }


}

