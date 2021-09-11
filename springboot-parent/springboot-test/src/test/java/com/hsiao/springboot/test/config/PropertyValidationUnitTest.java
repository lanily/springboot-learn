package com.hsiao.springboot.test.config;




import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hsiao.springboot.test.entity.MailServer;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: PropertyValidationUnitTest
 * @description: TODO
 * @author xiao
 * @create 2021/4/22
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = MailServer.class)
@TestPropertySource("classpath:property-validation-test.properties")
public class PropertyValidationUnitTest {

    @Autowired
    private MailServer mailServer;

    private static Validator propertyValidator;

    @BeforeAll
    public static void setup() {
        propertyValidator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void whenBindingPropertiesToValidatedBeans_thenConstrainsAreChecked() {
        assertEquals(0, propertyValidator.validate(mailServer.getPropertiesMap())
                .size());
        assertEquals(0, propertyValidator.validate(mailServer.getMailConfig())
                .size());
    }
}
