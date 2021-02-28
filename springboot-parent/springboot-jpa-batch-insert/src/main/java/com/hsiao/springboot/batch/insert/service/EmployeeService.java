/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeService Author: xiao Date: 2020/10/24 20:40
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert.service;

import com.hsiao.springboot.batch.insert.entity.Employee;
import com.hsiao.springboot.batch.insert.repository.DynamicQuery;
import com.hsiao.springboot.batch.insert.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

/**
 * Employee Service
 *
 * @projectName springboot-parent
 * @title: EmployeeService
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Service
public class EmployeeService {
  @Autowired EmployeeRepository employeeRepository;

  @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
  private int batchSize;

  @Autowired private DynamicQuery dynamicQuery;
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate; // 批量插入

  public void saveAll(List<Employee> employees) {
    employeeRepository.saveAll(employees);
  }

  public void batchEmployees() {

    List<Employee> employees = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      Employee employee = new Employee();
      employee.setId((long) i + 1);
      employee.setFirstName("Name_" + i);
      employee.setLastName("Genre_" + i);

      employees.add(employee);

      if (i % batchSize == 0 && i > 0) {
        employeeRepository.saveAll(employees);
        employees.clear();
      }
    }

    if (employees.size() > 0) {
      employeeRepository.saveAll(employees);
      employees.clear();
    }
  }

  public void batchSave() {
    List<Employee> employees = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      Employee employee = new Employee();
      employee.setId((long) i + 1);
      employee.setFirstName("Name_" + i);
      employee.setLastName("Genre_" + i);
      employees.add(employee);
    }
    SqlParameterSource[] beanSources = SqlParameterSourceUtils.createBatch(employees.toArray());
    String sql = "INSERT INTO app_student(class_id,name,age) VALUES (:classId,:name,:age) ";
    namedParameterJdbcTemplate.batchUpdate(sql, beanSources);
  }
}
