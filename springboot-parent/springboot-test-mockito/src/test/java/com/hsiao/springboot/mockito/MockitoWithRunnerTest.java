package com.hsiao.springboot.mockito;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hsiao.springboot.mockito.entity.Employee;
import com.hsiao.springboot.mockito.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * 方式三：在测试用例加上@RunWith(MockitoJUnitRunner.class)这个注解后，
 * 就可以自由的使用@Mock来mock对象
 *
 * 注：JUnit4
 * @projectName springboot-parent
 * @title: MockitoWithRunnerTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoWithRunnerTest {
    @Mock
    private EmployeeService employeeService;

    @Test
    public void testMock() {
        assertNotNull(employeeService);
        Employee employee = new Employee("bob");
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        employeeService.getEmployeeById(1L);
        verify(employeeService).getEmployeeById(1L);
    }
}

