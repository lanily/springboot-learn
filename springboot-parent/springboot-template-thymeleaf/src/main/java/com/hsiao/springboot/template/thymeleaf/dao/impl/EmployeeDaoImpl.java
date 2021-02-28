/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeDao Author: xiao Date: 2020/4/2 9:39 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.dao.impl;

import com.hsiao.springboot.template.thymeleaf.dao.DepartmentDao;
import com.hsiao.springboot.template.thymeleaf.dao.EmployeeDao;
import com.hsiao.springboot.template.thymeleaf.entity.Department;
import com.hsiao.springboot.template.thymeleaf.entity.Employee;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @projectName springboot-parent
 * @title: EmployeeDao
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
@Repository
public class EmployeeDaoImpl implements EmployeeDao {
  private static Map<Integer, Employee> employees = null;
  private static Integer initId = 1006;

  static {
    employees = new HashMap<Integer, Employee>();

    employees.put(1001, new Employee(1001, "E-AA", "aa@163.com", 1, new Department(101, "D-AA")));
    employees.put(1002, new Employee(1002, "E-BB", "bb@163.com", 1, new Department(102, "D-BB")));
    employees.put(1003, new Employee(1003, "E-CC", "cc@163.com", 0, new Department(103, "D-CC")));
    employees.put(1004, new Employee(1004, "E-DD", "dd@163.com", 0, new Department(104, "D-DD")));
    employees.put(1005, new Employee(1005, "E-EE", "ee@163.com", 1, new Department(105, "D-EE")));
  }

  private final Logger log = LoggerFactory.getLogger(EmployeeDaoImpl.class);
  @Autowired private DepartmentDao departmentDao;
  @Autowired private JdbcTemplate jdbcTemplate;

  @Override
  public void save(Employee employee) {
    if (employee.getId() == null) {
      employee.setId(initId++);
    }

    employee.setDepartment(departmentDao.getDepartment(employee.getDepartment().getId()));
    employees.put(employee.getId(), employee);
  }

  @Override
  public Collection<Employee> getEmployees() {
    return employees.values();
  }

  @Override
  public Employee get(Integer id) {
    return employees.get(id);
  }

  @Override
  public void delete(Integer id) {
    employees.remove(id);
  }

  @Override
  public List<Employee> getAll() {
    log.info("Querying all employees by getAll() ...");

    String sql =
        "select a.id, a.name, a.gender, a.birthday, a.email, b.id as deptId, b.name as deptName"
            + " from t_employee a left outer join t_department b "
            + " on a.dept_id = b.id "
            + " where 1 = 1";
    List<Employee> employees = new ArrayList<>();
    employees =
        jdbcTemplate.query(
            sql,
            new RowMapper<Employee>() {
              @Override
              public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setGender(rs.getInt("gender"));
                employee.setBirthday(rs.getDate("birthday"));
                employee.setEmail(rs.getString("email"));
                Department department = new Department();
                department.setId(rs.getInt("deptId"));
                department.setName(rs.getString("deptName"));
                employee.setDepartment(department);
                return employee;
              }
            });
    return employees;
  }
}
