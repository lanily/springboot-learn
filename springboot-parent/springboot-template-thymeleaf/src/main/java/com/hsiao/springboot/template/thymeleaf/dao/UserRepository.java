/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: UserRepository Author: xiao Date: 2020/4/1 9:49 下午
 * Description: History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.dao;

import com.hsiao.springboot.template.thymeleaf.entity.User;
import java.util.List;

/**
 * 用户仓库
 *
 * @author xiao
 * @create 2020/4/1
 * @since 1.0.0
 */
public interface UserRepository {
  /**
   * 新增或者修改用户
   *
   * @param user
   * @return
   */
  User saveOrUpateUser(User user);

  /**
   * 删除用户
   *
   * @param id
   */
  void deleteUser(Long id);

  /**
   * 根据用户id获取用户
   *
   * @param id
   * @return
   */
  User getUserById(Long id);

  /**
   * 获取所有用户的列表
   *
   * @return
   */
  List<User> listUser();
}
