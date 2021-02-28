/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeServiceTest Author: xiao Date: 2020/11/14
 * 09:20 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache;

import com.hsiao.springboot.ehcache.entity.Employee;
import com.hsiao.springboot.ehcache.exception.RecordNotFoundException;
import com.hsiao.springboot.ehcache.service.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceTest.class);

  @Autowired EmployeeService employeeService;
  List<Employee> employees = new ArrayList<>();

  @Before
  public void before() {
      Employee emp1 = new Employee(1L, "Alex", "Gussi");
      Employee emp2 = new Employee(2L, "Brian", "Schultz");
      Employee emp3 = new Employee(3L, "Geoff", "Gibson");
      Employee emp4 = new Employee(4L, "Cory", "Beck");
      Employee emp5 = new Employee(5L, "John", "Doe");
      Employee emp6 = new Employee(6L, "Bob", "Kobe");
      employees.add(emp1);
      employees.add(emp2);
      employees.add(emp3);
      employees.add(emp4);
      employees.add(emp5);
      employees.add(emp6);
  }

  @Test
  public void testSaveOrUpdateEmployee() {
      try{
          for(Employee emp : employees) {
              employeeService.createOrUpdateEmployee(emp);
          }
          Employee emp = new Employee(1L, "Alex", "Gussi");
          employeeService.createOrUpdateEmployee(emp);
      } catch (RecordNotFoundException e) {
          e.printStackTrace();
      }
  }

  @Test
  public void testGetEmployee() {
      try{
          Employee emp = new Employee(1L, "Alex", "Gussi");
          employeeService.createOrUpdateEmployee(emp);
          employeeService.getEmployee(1L);
      } catch (RecordNotFoundException e) {
          e.printStackTrace();
      }
  }
}
