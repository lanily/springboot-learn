package com.hsiao.springboot.test.repository;


import com.hsiao.springboot.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: UserRepository
 * @description: TODO
 * @author xiao
 * @create 2021/4/11
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}


