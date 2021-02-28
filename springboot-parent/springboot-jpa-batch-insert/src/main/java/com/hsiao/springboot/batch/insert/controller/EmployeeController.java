/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: PepoleController Author: xiao Date: 2020/10/24 13:06
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert.controller;

import com.hsiao.springboot.batch.insert.entity.Employee;
import com.hsiao.springboot.batch.insert.repository.EmployeeRepository;
import com.hsiao.springboot.batch.insert.service.BatchService;
import com.hsiao.springboot.batch.insert.service.EmployeeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: PepoleController
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@RequestMapping("/employees")
@RestController
@Slf4j
public class EmployeeController {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

  @Autowired private EmployeeRepository employeerepository;
  @Autowired private EmployeeService employeeService;

  @Autowired private BatchService batchService;

  @PostMapping("/employees")
  public ResponseEntity<String> insertEmployees() {
    Employee e1 = new Employee("James", "Gosling");
    Employee e2 = new Employee("Doug", "Lea");
    Employee e3 = new Employee("Martin", "Fowler");
    Employee e4 = new Employee("Brian", "Goetz");
    List<Employee> employees = Arrays.asList(e1, e2, e3, e4);
    employeeService.saveAll(employees);
    //        return ResponseEntity.created("/employees");
    return null;
  }

  @GetMapping("/save")
  public void save() {
    List<Employee> list = new ArrayList<>();
    for (int i = 0; i < 500; i++) {
      Employee employee = new Employee();
      employee.setFirstName("bob" + i);
      employee.setLastName("brain " + i);
      list.add(employee);
    }
    long start = System.currentTimeMillis();
    log.info("开始保存", start);
    // 500 条数据花费9s
    employeerepository.saveAll(list);
    long end = System.currentTimeMillis();
    log.info("耗时{}s", (end - start) / 1000);
  }

  @GetMapping("/saveAll")
  public void saveAll() {
    List<Employee> list = new ArrayList<>();
    for (int i = 0; i < 500; i++) {
      Employee employee = new Employee();
      employee.setFirstName("bob" + i);
      employee.setLastName("brain " + i);
      list.add(employee);
    }
    long start = System.currentTimeMillis();
    log.info("开始保存", start);
    // 500 条数据花费13s  yml开启批量操作9s
    batchService.batchInsert(list);
    long end = System.currentTimeMillis();
    log.info("耗时{}s", (end - start) / 1000);
  }

  /** 使用原生的jdbcTemplate批量插入数据 速度比上面的两个都快 */
  @GetMapping("/saveBatch")
  public void saveBatch() {
    // 耗时1792ms 速度很快了
    List<Employee> list = new ArrayList<>();
    for (int i = 0; i < 500; i++) {
      Employee employee = new Employee();
      employee.setFirstName("bob" + i);
      employee.setLastName("brain " + i);
      list.add(employee);
    }
    long start = System.currentTimeMillis();
    log.info("开始保存", start);
    batchService.saveBatch(list);
    long end = System.currentTimeMillis();
    log.info("耗时{}", end - start);
  }
}
