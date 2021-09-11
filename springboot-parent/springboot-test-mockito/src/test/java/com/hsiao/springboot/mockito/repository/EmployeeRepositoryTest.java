package com.hsiao.springboot.mockito.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.hsiao.springboot.mockito.entity.Employee;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 关于webEnvironment，有MOCK、RANDOM_PORT、DEFINED_PORT、NONE四个选项，其中：
 *
 * MOCK—提供一个虚拟的Servlet环境，内置的Servlet容器并没有真实的启动
 * RANDOM_PORT提供一个真实的Servlet环境，也就是说会启动内置容器，然后使用的是随机端口
 * DEFINED_PORT这个配置也是提供一个真实的Servlet环境，使用的配置文件中配置的端口，如果没有配置，默认是8080
 *
 * @projectName springboot-parent
 * @title: EmployeeRepositoryTest
 * @description: TODO
 * @author xiao
 * @create 2021/4/21
 * @since 1.0.0
 */

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = TestApplication.class)
//@SpringBootTest(
//        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//        classes = TestApplication.class)
@DataJpaTest
//@TestPropertySource(locations = "classpath:application-test.properties")
//@AutoConfigureTestDatabase(replace = Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
//@ActiveProfiles("test")
public class EmployeeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        Employee alex = new Employee("alex");
        entityManager.persistAndFlush(alex);

        Employee found = employeeRepository.findByName(alex.getName());
        assertThat(found.getName()).isEqualTo(alex.getName());
    }

    @Test
    public void whenInvalidName_thenReturnNull() {
        Employee fromDb = employeeRepository.findByName("doesNotExist");
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindById_thenReturnEmployee() {
        Employee emp = new Employee("test");
        entityManager.persistAndFlush(emp);

        Employee fromDb = employeeRepository.findById(emp.getId()).orElse(null);
        assertThat(fromDb.getName()).isEqualTo(emp.getName());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        Employee fromDb = employeeRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
        Employee alex = new Employee("alex");
        Employee ron = new Employee("ron");
        Employee bob = new Employee("bob");

        entityManager.persist(alex);
        entityManager.persist(bob);
        entityManager.persist(ron);
        entityManager.flush();

        List<Employee> allEmployees = employeeRepository.findAll();

        assertThat(allEmployees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
    }
}

