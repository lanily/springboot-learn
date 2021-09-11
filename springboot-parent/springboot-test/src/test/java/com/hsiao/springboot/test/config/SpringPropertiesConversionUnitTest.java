package com.hsiao.springboot.test.config;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hsiao.springboot.test.convert.CustomCredentialsConverter;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.unit.DataSize;


/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: SpringPropertiesConversionUnitTest
 * @description: TODO
 * @author xiao
 * @create 2021/4/22
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = PropertyConversion.class)
@ContextConfiguration(classes = CustomCredentialsConverter.class)
@TestPropertySource("classpath:spring-conversion-test.properties")
public class SpringPropertiesConversionUnitTest {

    @Autowired
    private PropertyConversion propertyConversion;

    @Test
    void whenUsingSpringDefaultSizeConversion_thenDataSizeObjectIsSet() {
        assertEquals(DataSize.ofMegabytes(500), propertyConversion.getUploadSpeed());
        assertEquals(DataSize.ofGigabytes(10), propertyConversion.getDownloadSpeed());
    }

    @Test
    void whenUsingSpringDefaultDurationConversion_thenDurationObjectIsSet() {
        assertEquals(Duration.ofDays(1), propertyConversion.getBackupDay());
        assertEquals(Duration.ofHours(8), propertyConversion.getBackupHour());
    }

    @Test
    void whenRegisteringCustomCredentialsConverter_thenCredentialsAreParsed() {
        assertEquals("user", propertyConversion.getCredentials()
                .getUsername());
        assertEquals("123", propertyConversion.getCredentials()
                .getPassword());
    }
}

