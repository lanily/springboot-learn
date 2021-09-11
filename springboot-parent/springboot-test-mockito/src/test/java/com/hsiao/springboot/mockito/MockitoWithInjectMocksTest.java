package com.hsiao.springboot.mockito;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.hsiao.springboot.mockito.entity.Employee;
import com.hsiao.springboot.mockito.repository.EmployeeRepository;
import com.hsiao.springboot.mockito.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * mock对象被注入另一个对象(@Spy注解的也会被注入)
 *
 * @projectName springboot-parent
 * @title: MockitoWithMockTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
@SpringBootTest
public class MockitoWithInjectMocksTest {

    // mock一个对象，不做stub，调用其方法默认返回null
    // mock一个对象，默认调用真实方法，除非手动when().thenReturn()

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Before
    public void before() {
        assertNotNull(employeeService);
    }

    @Test
    public void testMock() {
        Employee employee = new Employee("bob");
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));
        Employee ret = employeeService.getEmployeeById(1L);
        assertNotNull(ret);
    }
}

