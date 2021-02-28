/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeCacheTest Author: xiao Date: 2020/10/24 20:46
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache;

import com.hsiao.springboot.ehcache.entity.Employee;
import com.hsiao.springboot.ehcache.service.EmployeeManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * Employee Cache Test
 * @projectName springboot-parent
 * @title: EmployeeCacheTest
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeCacheTest {

  @Autowired private CacheManager cacheManager;

  @Autowired private EmployeeManager employeeManager;

  @Test
  public void testCache() {
    // This will hit the database
    System.out.println(employeeManager.getEmployeeById(1L));

    // This will hit the cache - verify the message in console output
    System.out.println(employeeManager.getEmployeeById(1L));

    // Access cache instance by name
    Cache cache = cacheManager.getCache("employeeCache");

    // Add entry to cache
    cache.put(3L, new Employee(3L, "Charles", "Dave"));

    // Get entry from cache
    System.out.println(cache.get(3L).get());
  };
}
