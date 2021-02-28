/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: PepoleController Author: xiao Date: 2020/10/24 13:06
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache.contoller;

import com.hsiao.springboot.ehcache.entity.Employee;
import com.hsiao.springboot.ehcache.exception.RecordNotFoundException;
import com.hsiao.springboot.ehcache.service.EmployeeService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
public class EmployeeController {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

  @Autowired private EmployeeService employeeService;

  @GetMapping("/")
  public ResponseEntity<List<Employee>> getAllEmployees() {
    List<Employee> employees = employeeService.getAllEmployees();
    return new ResponseEntity<>(employees, new HttpHeaders(), HttpStatus.OK);
  }

  @RequestMapping("/get")
  public Employee getEmployee(Long id) {
    Employee employee = employeeService.getEmployee(id).get();
    LOG.info("读取到数据 " + employee.getFirstName() + "," + employee.getLastName());
    return employee;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long id)
      throws RecordNotFoundException {
    Employee employee = employeeService.getEmployeeById(id);
    return new ResponseEntity<Employee>(employee, HttpStatus.OK);
  }

  @PutMapping()
  public ResponseEntity<Employee> createOrUpdateEmployee(Employee employee)
      throws RecordNotFoundException {
    Employee updated = employeeService.createOrUpdateEmployee(employee);
    return new ResponseEntity<Employee>(updated, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public HttpStatus deleteEmployeeById(@PathVariable(value = "id") Long id)
      throws RecordNotFoundException {
    employeeService.deleteEmployeeById(id);
    return HttpStatus.FORBIDDEN;
  }
}
