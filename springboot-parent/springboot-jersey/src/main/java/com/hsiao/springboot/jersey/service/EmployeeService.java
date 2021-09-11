package com.hsiao.springboot.jersey.service;


import com.hsiao.springboot.jersey.exception.ResourceNotFoundException;
import com.hsiao.springboot.jersey.model.Employee;
import com.hsiao.springboot.jersey.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EmployeeService
 * @description: TODO
 * @author xiao
 * @create 2021/8/7
 * @since 1.0.0
 */
@Service
@Singleton
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee find(final Long employeeId) {
        Employee e = null;
        Optional<Employee> option = employeeRepository.findById(employeeId);
        if (option.isPresent()) {
            e = option.get();
        }
        return e;
//                employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee save(final Employee employee) {
        return employeeRepository.save(employee);
    }


    public Employee update(final Long employeeId, final Employee employee) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        e.setName(employee.getName());
        e.setAddress(employee.getAddress());
        Employee updatedEmployee = employeeRepository.save(e);
        return updatedEmployee;
    }


    public void delete(final Long employeeId) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        employeeRepository.delete(e);
    }
}

