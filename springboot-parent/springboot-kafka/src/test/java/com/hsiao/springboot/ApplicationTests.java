package com.hsiao.springboot;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTests.class)
@EmbeddedKafka(count = 4,ports = {9092,9093,9094,9095})
//@EmbeddedKafka(brokerProperties = {"log.index.interval.bytes = 4096","num.io.threads = 8"})
//@EmbeddedKafka(brokerPropertiesLocation = "classpath:application.properties")
public class ApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        System.in.read();
    }
}
