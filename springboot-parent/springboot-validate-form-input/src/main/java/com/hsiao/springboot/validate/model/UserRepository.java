/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: UserRepository Author:   xiao Date:     2020/3/30
 * 11:06 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.validate.model;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户持久层操作接口
 * @projectName springboot-parent
 * @title: UserRepository
 * @description: TODO
 * @author xiao
 * @create 2020/3/30
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

}

