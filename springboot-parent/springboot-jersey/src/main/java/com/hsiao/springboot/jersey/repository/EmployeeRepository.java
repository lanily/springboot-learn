package com.hsiao.springboot.jersey.repository;


import com.hsiao.springboot.jersey.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EmployeeRepository
 * @description: TODO
 * @author xiao
 * @create 2021/8/8
 * @since 1.0.0
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Modifying
    @Query("update Employee e set e.address= :address where e.id= :id")
    void updateAddress(Long id, String address);
}

