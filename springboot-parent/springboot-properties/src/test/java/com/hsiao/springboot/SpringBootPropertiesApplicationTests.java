/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: ApplicationTests Author: xiao Date: 2019/11/16 9:38
 * AM History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hsiao.springboot.properties.entity.ConfigurationPropertiesEntity;
import com.hsiao.springboot.properties.entity.Person;
import com.hsiao.springboot.properties.entity.PersonSource;
import com.hsiao.springboot.properties.entity.PersonValue;
import com.hsiao.springboot.properties.entity.RandomEntity;
import com.hsiao.springboot.properties.entity.User;
import com.hsiao.springboot.properties.entity.ValueEntity;
import com.hsiao.springboot.properties.entity.YamlEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * spring property test<br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: ApplicationTests
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootPropertiesApplicationTests {

  @Autowired private MockMvc mvc;

  @Autowired private Person person;

  @Autowired private PersonValue personValue;

  @Autowired private PersonSource personSource;
  @Autowired private User user;
  @Autowired private YamlEntity yamlEntity;
  @Autowired private ConfigurationPropertiesEntity configurationPropertiesEntity;
  @Autowired private ValueEntity valueEntity;
  @Autowired private RandomEntity randomEntity;

  /** 模拟请求测试 */
  @Test
  public void testGetHello() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Greetings from Spring Boot!"));
  }

  /** 测试@ConfigurationProperties */
  @Test
  public void testPersion() {
    System.out.println(person);
  }

  /** 测试@Value 引入配置值 */
  @Test
  public void testPersionValue() {
    System.out.println(personValue);
  }

  /** 测试#PropertySource 引入配置值 */
  @Test
  public void testPersionSource() {
    System.out.println(personSource);
  }

  @Test
  public void contextLoads() {
    //      System.out.println("YAML Grammar : " + yamlEntity);
    //      System.out.println("User : " + user);
    //      System.out.println("ConfigurationProperties Grammar : " +
    // configurationPropertiesEntity);
    //      System.out.println("Value Grammar : " + valueEntity);
    System.out.println("Random Grammar : " + randomEntity);
  }
}
