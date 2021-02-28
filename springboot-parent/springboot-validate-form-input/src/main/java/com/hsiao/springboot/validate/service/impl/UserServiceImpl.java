/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: UserServiceImpl Author: xiao Date: 2020/3/30 11:10 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.validate.service.impl;

import com.hsiao.springboot.validate.model.User;
import com.hsiao.springboot.validate.model.UserRepository;
import com.hsiao.springboot.validate.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User 业务层实现
 *
 * @projectName springboot-parent
 * @title: UserServiceImpl
 * @description: TODO
 * @author xiao
 * @create 2020/3/30
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired UserRepository userRepository;

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User insertByUser(User user) {
    LOGGER.info("新增用户：" + user.toString());
    return userRepository.save(user);
  }

  @Override
  public User update(User user) {
    LOGGER.info("更新用户：" + user.toString());
    return userRepository.save(user);
  }

  @Override
  public User delete(Long id) {
    User user = userRepository.findById(id).get();
    userRepository.delete(user);

    LOGGER.info("删除用户：" + user.toString());
    return user;
  }

  @Override
  public User findById(Long id) {
    LOGGER.info("获取用户 ID ：" + id);
    return userRepository.findById(id).get();
  }
}
