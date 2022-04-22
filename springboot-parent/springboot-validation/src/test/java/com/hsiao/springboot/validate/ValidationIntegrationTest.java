package com.hsiao.springboot.validate;

import static org.junit.Assert.assertEquals;

import com.hsiao.springboot.validate.model.Job;
import com.hsiao.springboot.validate.model.User;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ValidationIntegrationTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
public class ValidationIntegrationTest {

    private Validator validator;

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static <T> List<String> validate(T t) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            messageList.add(constraintViolation.getMessage());
        }
        return messageList;
    }

    @Before
    public void setup() {
        validator = factory.getValidator();
    }

    private static User createUser() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setPassword("Admin123");
        user.setBirthday("1991-01-18");
        user.setHeight(BigDecimal.valueOf(180.18));
        user.setEmail("309183912@qq.com");
        user.setPhone("15818584558");
        user.setIdCard("477376197205082595");
        try {
            user.setJoinTime(sdf.parse("2014-09-01"));
            user.setResignTime(sdf.parse("2025-09-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setSex("MAN");
        user.setAge(18);

        Job job = new Job();
        job.setId(1L);
        job.setName("Boss");
        job.setOffice(OptionalInt.of(1));
        job.setPosition("CEO");
        job.setSalary(BigDecimal.valueOf(10000L));
        job.setWorking(true);
        job.setCreateTime("2021-01-01");
        job.setAddresses(Collections.singletonList("shanghai"));
        user.setJob(job);
        return user;
    }

    @Test
    public void test_validateUser() {
        User user = createUser();
        List<String> validate = validate(user);
        validate.forEach(row -> {
            System.out.println(row.toString());
        });
    }


    @Test
    public void test_idCard() {
        User user = createUser();
//        user.setIdCard("470100199010189439");
        user.setIdCard("511111194910253124");
        List<String> validate = validate(user);
        validate.forEach(row -> {
            System.out.println(row.toString());
        });

    }

    @Test
    public void ifNameIsNull_nameValidationFails() {
        User user = createUser();
        user.setName(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(violations.isEmpty(), false);
    }


    @Test
    public void givenEmptyOptional_thenValidationSucceeds() {
        User user = createUser();
        Job job = user.getJob();
        job.setOffice(OptionalInt.empty());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

}

