package com.hsiao.springboot.test.service;


import com.hsiao.springboot.test.entity.User;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: UserService
 * @description: TODO
 * @author xiao
 * @create 2021/4/11
 * @since 1.0.0
 */
public interface UserService {

    User findOne(Long id);

    boolean updateUsername(Long id, String usernmae);

    User save(User user);

    User findByUsername(String username);
}

