package com.hsiao.springboot.mockito;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hsiao.springboot.mockito.entity.Employee;
import com.hsiao.springboot.mockito.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * 方式一：直接使用Mockito提供的mock方法即可模拟出一个服务的实例。
 * 再结合when/thnReturn等语法完成方法的模拟实现
 *
 * @projectName springboot-parent
 * @title: MockitoWithMockTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
@SpringBootTest
public class MockitoWithMockTest {

    private EmployeeService employeeService;

    @Before
    public void before() {
        // mock一个对象，不做stub，调用其方法默认返回null
        // mock一个对象，默认调用真实方法，除非手动when().thenReturn()
        employeeService = mock(EmployeeService.class);
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

