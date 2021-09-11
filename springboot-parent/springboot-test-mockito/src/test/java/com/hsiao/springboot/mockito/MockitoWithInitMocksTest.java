package com.hsiao.springboot.mockito;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hsiao.springboot.mockito.entity.Employee;
import com.hsiao.springboot.mockito.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * 方式二：使用@Mock注解来Mock对象，即使用
 * MockitoAnnotations.initMocks(this);
 *
 * @projectName springboot-parent
 * @title: MockitoTest
 * @description: TODO
 * @author xiao
 * @create 2021/7/22
 * @since 1.0.0
 */
@SpringBootTest
public class MockitoWithInitMocksTest {

    @Mock
    private EmployeeService employeeService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(employeeService);
    }

    @Test
    public void testMock() {
        Employee employee = new Employee("bob");
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        employeeService.getEmployeeById(1L);
        verify(employeeService).getEmployeeById(1L);
    }

}


