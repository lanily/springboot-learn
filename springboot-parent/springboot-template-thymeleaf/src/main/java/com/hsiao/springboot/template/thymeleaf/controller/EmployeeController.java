/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeController Author: xiao Date: 2020/4/2 10:03
 * 下午 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.controller;

import com.hsiao.springboot.template.thymeleaf.dao.DepartmentDao;
import com.hsiao.springboot.template.thymeleaf.dao.EmployeeDao;
import com.hsiao.springboot.template.thymeleaf.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 员工控制器
 *
 * @projectName springboot-parent
 * @title: EmployeeController
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired EmployeeDao employeeDao;

  @Autowired DepartmentDao departmentDao;

  @GetMapping("/emps")
  public String list(Model model) {
    model.addAttribute("emps", employeeDao.getAll());
    return "emps/list";
  }

  @RequestMapping(value = {"", "/", "/dashboard"})
  public String dashboard() {
    return "dashboard";
  }

  // forward to /WEB-INF/templates/employee/list.html
  /*@RequestMapping("/emps")
  public String emps(Map<String, Object> map) {
      map.put("departments", departmentDao.getAll());
      System.out.println(departmentDao.getAll());
      return "employee/list";
  }*/

  /**
   * 来到添加页面
   *
   * @return
   */
  @GetMapping("/emp")
  public String toAddPage(Model model) {
    model.addAttribute("departments", departmentDao.getDepartments());
    return "emps/add";
  }

  /**
   * 添加员工 SpringMVC自动将请求参数和入参对象的属性进行一一绑定； 要求请求参数的名字和javaBean入参的对象里面的属性名是一样的
   *
   * @return
   */
  @PostMapping("/emp")
  public String addEmployee(Employee employee) {
    employeeDao.save(employee);
    return "redirect:/emps";
  }

  /**
   * 来到修改页面
   *
   * @return
   */
  @GetMapping("/emp/{id}")
  public String toEditPage(@PathVariable("id") Integer id, Model model) {
    Employee employee = employeeDao.get(id);
    model.addAttribute("emp", employee);
    model.addAttribute("departments", departmentDao.getDepartments());
    return "emps/add";
  }

  /**
   * 修改员工
   *
   * @param employee
   * @return
   */
  @PutMapping("/emp")
  public String editEmployee(Employee employee) {
    employeeDao.save(employee);
    return "redirect:/emps";
  }

  /**
   * 删除员工
   *
   * @param id
   * @return
   */
  @DeleteMapping("/emp/{id}")
  public String removeEmployee(@PathVariable("id") Integer id) {
    employeeDao.delete(id);
    return "redirect:/emps";
  }
}
