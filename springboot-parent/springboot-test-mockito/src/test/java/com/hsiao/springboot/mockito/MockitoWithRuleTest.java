package com.hsiao.springboot.mockito;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hsiao.springboot.mockito.entity.Employee;
import com.hsiao.springboot.mockito.service.EmployeeService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * 方式四：使用MockitoRule。
 * 这里需要注意的是如果使用MockitoRule的话，该对象的访问级别必须是public。
 * 注意：JUnit4
 *
 * @projectName springboot-parent
 * @title: MockitoWithMockTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
@RunWith(JUnit4.class)
@SpringBootTest
public class MockitoWithRuleTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private EmployeeService employeeService;

    @Before
    public void before() {
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

