/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: UserService Author: xiao Date: 2020/3/30 11:09 下午
 * Description: History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.validate.service;

import com.hsiao.springboot.validate.model.User;
import java.util.List;

/**
 * User 业务层接口
 *
 * @author xiao
 * @create 2020/3/30
 * @since 1.0.0
 */
public interface UserService {

  List<User> findAll();

  User insertByUser(User user);

  User update(User user);

  User delete(Long id);

  User findById(Long id);
}
