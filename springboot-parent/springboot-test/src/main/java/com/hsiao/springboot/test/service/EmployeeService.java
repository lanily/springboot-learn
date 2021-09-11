package com.hsiao.springboot.test.service;


import com.hsiao.springboot.test.entity.Employee;
import java.util.List;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EmployeeService
 * @description: TODO
 * @author xiao
 * @create 2021/4/21
 * @since 1.0.0
 */
public interface EmployeeService {

    public Employee getEmployeeById(Long id);

    public Employee getEmployeeByName(String name);

    public List<Employee> getAllEmployees();

    public boolean exists(String email);

    public Employee save(Employee employee);
}

