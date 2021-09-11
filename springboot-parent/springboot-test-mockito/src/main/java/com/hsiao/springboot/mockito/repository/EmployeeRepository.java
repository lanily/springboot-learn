package com.hsiao.springboot.mockito.repository;


import com.hsiao.springboot.mockito.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EmployeeRepository
 * @description: TODO
 * @author xiao
 * @create 2021/4/21
 * @since 1.0.0
 */
@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public Employee findByName(String name);

    @Override
    public List<Employee> findAll();
}

