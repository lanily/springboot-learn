/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: UserRepositoryImpl Author: xiao Date: 2020/4/1 9:50
 * 下午 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.dao.impl;

import com.hsiao.springboot.template.thymeleaf.dao.UserRepository;
import com.hsiao.springboot.template.thymeleaf.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

/**
 * 用户资源库实现
 *
 * @projectName springboot-parent
 * @title: UserRepositoryImpl
 * @description: TODO
 * @author xiao
 * @create 2020/4/1
 * @since 1.0.0
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

  private static AtomicLong counter = new AtomicLong();

  private final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<Long, User>();

  public UserRepositoryImpl() {
    User user = new User();
    user.setName("Tom");
    user.setAge(20);
    this.saveOrUpateUser(user);
  }

  @Override
  public User saveOrUpateUser(User user) {
    Long id = user.getId();
    if (id <= 0) {
      id = counter.incrementAndGet();
      user.setId(id);
    }
    this.userMap.put(id, user);
    return user;
  }

  @Override
  public void deleteUser(Long id) {
    this.userMap.remove(id);
  }

  @Override
  public User getUserById(Long id) {
    return this.userMap.get(id);
  }

  @Override
  public List<User> listUser() {
    return new ArrayList<User>(this.userMap.values());
  }
}
