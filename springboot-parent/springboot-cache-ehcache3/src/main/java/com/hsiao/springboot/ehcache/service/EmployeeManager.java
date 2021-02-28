/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeManager Author:   xiao Date:     2020/11/14
 * 07:52 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.service;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * @projectName springboot-parent
 * @title: EmployeeManager
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */

import com.hsiao.springboot.ehcache.entity.Employee;
import java.util.HashMap;
import org.springframework.cache.annotation.Cacheable;

/**
 * Employee Cache Manager
 * https://howtodoinjava.com/spring-boot2/ehcache3-config-example/
 * @projectName springboot-parent
 * @title: EmployeeService
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
public class EmployeeManager {

    static HashMap<Long, Employee> db = new HashMap<>();

    static {
        db.put(1L, new Employee(1L, "Alex", "Gussin"));
        db.put(2L, new Employee(2L, "Brian", "Schultz"));
    }

    @Cacheable(cacheNames="employeeCache", key="#id")
    public Employee getEmployeeById(Long id) {
        System.out.println("Getting employee from DB");
        return db.get(id);
    }
}

