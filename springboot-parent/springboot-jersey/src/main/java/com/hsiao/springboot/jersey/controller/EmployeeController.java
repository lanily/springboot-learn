package com.hsiao.springboot.jersey.controller;


import com.hsiao.springboot.jersey.model.Employee;
import com.hsiao.springboot.jersey.service.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * api use spring rest controller
 *
 * @projectName springboot-parent
 * @title: EmployeeController
 * @description: TODO
 * @author xiao
 * @create 2021/8/8
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getAllComments() {
        return employeeService.findAll();
    }

    @PostMapping("/employees")
    public Employee createComment(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @GetMapping("/employees/{id}")
    public Employee getCommentById(@PathVariable(value = "id") Long employeeId) {
        return employeeService.find(employeeId);
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployee(@PathVariable(value = "id") Long employeeId, @RequestBody Employee employee) {
        return employeeService.update(employeeId, employee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "id") Long employeeId) {
        employeeService.delete(employeeId);
        return ResponseEntity.ok().build();
    }
}

