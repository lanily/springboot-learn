/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: Application Author: xiao Date: 2020/3/31 9:53 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.validate;

import com.hsiao.springboot.validate.model.User;
import com.hsiao.springboot.validate.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 表单校验案例
 *
 * @projectName springboot-parent
 * @title: Application
 * @description: TODO
 * @author xiao
 * @create 2020/3/31
 * @since 1.0.0
 */
@SpringBootApplication
public class SpringBootValidateInputApplication implements CommandLineRunner {

  private Logger LOG = LoggerFactory.getLogger(SpringBootValidateInputApplication.class);

  @Autowired private UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(SpringBootValidateInputApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    User user1 = new User("Sergey", 24, "1994-01-01");
    User user2 = new User("Ivan", 26, "1994-01-01");
    User user3 = new User("Adam", 31, "1994-01-01");
    LOG.info("Inserting data in DB.");
    userRepository.save(user1);
    userRepository.save(user2);
    userRepository.save(user3);
    LOG.info("User count in DB: {}", userRepository.count());
    LOG.info("User with ID 1: {}", userRepository.findById(1L));
    LOG.info("Deleting user with ID 2L form DB.");
    userRepository.deleteById(2L);
    LOG.info("User count in DB: {}", userRepository.count());
  }
}
