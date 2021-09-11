package com.hsiao.springboot.mockito;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.hsiao.springboot.mockito.entity.Employee;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: MockitoTest
 * @description: TODO
 * @author xiao
 * @create 2021/7/22
 * @since 1.0.0
 */
//注意1：@Test的测试方法必须是public的,否则：java.lang.Exception: Method XXX should be public
@RunWith(MockitoJUnitRunner.class)
public class MockitoQuickStartTest {

    @Before
    public void setup() {
        //这句话执行以后，aaaDao和bbbDao自动注入到abcService中。
        MockitoAnnotations.initMocks(this);
        //在这之后，你就可以放心大胆地使用when().then()、
        //Mockito.doNothing().when(obj).someMethod()、
        //doThrow(new RuntimeException()).when(obj).someMethod(Mockito.any());
        //等进行更详细的设置。
    }

    @After
    public void clearMocks() {
        // 避免大量内存泄漏  2.25.0新增
        Mockito.framework().clearInlineMocks();
    }

    @Test // 没返回值的方法匹配
    public void test0() {
        List mockList = mock(List.class);

        mockList.add(1); // 基础类型
        mockList.add(Lists.newArrayList("a")); // 引用类型

        verify(mockList).add(1);
        verify(mockList).add(Lists.newArrayList("a"));
    }


    @Test // 自定义返回结果
    public void test3() {
        Map mockMap = mock(Map.class);

        Answer answer = (invocation) -> {
            Object[] args = invocation.getArguments();
            return String.valueOf(args[0]) + 110;
        };
        when(mockMap.get(anyInt())).then(answer);
        TestCase.assertEquals("120110", mockMap.get(120));
    }

    @Test // 设置抛出异常
    public void test4() {
        List mockList = mock(List.class);

        // 1.无返回值方法 设置异常
        doThrow(RuntimeException.class).when(mockList).add(1);
        try {
            mockList.add(1);
        } catch (RuntimeException e) {
            System.out.println("doThrow(RuntimeException.class).when(mock).someVoidMethod();");
        }

        // 2.有返回值方法 设置异常
        when(mockList.get(1000)).thenThrow(new IndexOutOfBoundsException("数组下标越界"));
        try {
            mockList.get(1000);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test //连续的方法调用设置不同的行为
    public void test5() {
        List mockList = mock(List.class);
        when(mockList.get(1000)).thenReturn(11, 22)
                .thenThrow(new IndexOutOfBoundsException("数组下标越界"));
        TestCase.assertEquals(11, mockList.get(1000)); // 第一次调用返回值：11
        TestCase.assertEquals(22, mockList.get(1000)); // 第二次调用返回值：22
        try {
            mockList.get(1000); // 第三次调用抛出异常
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test // 测试方法的调用次数
    public void test6() {
        /*
         * times(n)：方法被调用n次。
         * never()：没有被调用。
         * atLeast(n)：至少被调用n次。
         * atLeastOnce()：至少被调用1次，相当于atLeast(1)。
         * atMost()：最多被调用n次。
         */
        List mockList = mock(List.class);
        mockList.add("one times");
        mockList.add("2 times");
        mockList.add("2 times");
        verify(mockList, atMost(1)).add("one times");
        verify(mockList, times(2)).add("2 times");
    }

    /**
     * 配置 Mock 对象
     * 当我们有了一个 Mock 对象后, 我们可以定制它的具体的行为.
     */
    @Test
    public void createMockObject() {
        // 使用 mock 静态方法创建 Mock 对象.
        List mockedList = mock(List.class);
        Assert.assertTrue(mockedList instanceof List);

        // mock 方法不仅可以 Mock 接口类, 还可以 Mock 具体的类型.
        ArrayList mockedArrayList = mock(ArrayList.class);
        Assert.assertTrue(mockedArrayList instanceof List);
        Assert.assertTrue(mockedArrayList instanceof ArrayList);
    }

    @Test
    public void test1Return() {
        //  创建 mock
        Employee test = mock(Employee.class);
        // 自定义 getId() 的返回值
        when(test.getId()).thenReturn(2L);
        // 在测试中使用mock对象
        assertEquals(Long.valueOf(2L), test.getId());
    }

    // 返回多个值
    @Test
    public void testMoreThanOneReturnValue() {
        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Mockito").thenReturn("rocks");
        String result = i.next() + " " + i.next();
        // 断言
        assertEquals("Mockito rocks", result);
    }

    // 如何根据输入来返回值
    @Test
    public void testReturnValueDependentOnMethodParameter() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo("Mockito")).thenReturn(1);
        when(c.compareTo("Eclipse")).thenReturn(2);
        // 断言
        assertEquals(1, c.compareTo("Mockito"));
    }

    // 如何让返回值不依赖于输入
    @Test
    public void testReturnValueInDependentOnMethodParameter() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo(anyInt())).thenReturn(-1);
        // 断言
        assertEquals(-1, c.compareTo(9));
    }

    // 下面测试用例描述了如何使用doThrow()方法
    @Test(expected = IOException.class)
    public void testForIOException() throws IOException {
        // 创建并配置 mock 对象
        OutputStream mockStream = mock(OutputStream.class);
        doThrow(new IOException()).when(mockStream).close();

        // 使用 mock
        OutputStreamWriter streamWriter = new OutputStreamWriter(mockStream);
        streamWriter.close();
    }

    /**
     * 校验 Mock 对象的方法调用
     * Mockito 会追踪 Mock 对象的所用方法调用和调用方法时所传递的参数. 我们可以通过 verify() 静态方法来来校验指定的方法调用是否满足断言.
     */
    @Test
    public void testVerify() {
        // 创建并配置 mock 对象
        List mockedList = mock(List.class);
        when(mockedList.size()).thenReturn(5);
        Assert.assertEquals(mockedList.size(), 5);

        // 调用mock对象里面的方法并传入参数
        mockedList.add("one");
        mockedList.add("two");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        // 查看在传入参数为one的时候方法是否被调用
        verify(mockedList).add("one");

        // 方法是否被调用3次
        verify(mockedList, times(3)).add("three times");

        // 其他用来验证函数是否被调用的方法a
        verify(mockedList, atLeastOnce()).add("called at least once");
        verify(mockedList, atLeast(2)).add("called at least twice");
        verify(mockedList, times(5)).add("called five times");
        verify(mockedList, atMost(3)).add("called at most 3 times");
    }

    /**
     * 使用 spy() 部分模拟对象
     * Mockito 提供的 spy 方法可以包装一个真实的 Java 对象, 并返回一个包装后的新对象. 若没有特别配置的话, 对这个新对象的所有方法调用, 都会委派给实际的 Java 对象.
     */
    @Test
    public void testSpy() {
        List list = new LinkedList();
        List spy = spy(list);

        // 对 spy.size() 进行定制.
        when(spy.size()).thenReturn(100);

        spy.add("one");
        spy.add("two");

        // 因为我们没有对 get(0), get(1) 方法进行定制,
        // 因此这些调用其实是调用的真实对象的方法.
        Assert.assertEquals(spy.get(0), "one");
        Assert.assertEquals(spy.get(1), "two");

        Assert.assertEquals(spy.size(), 100);
    }

    /**
     * 参数捕获
     * Mockito 允准我们捕获一个 Mock 对象的方法调用所传递的参数.
     */
    @Test
    public void testCaptureArgument() {
        List<String> list = Arrays.asList("1", "2");
        List mockedList = mock(List.class);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        mockedList.addAll(list);
        verify(mockedList).addAll(argument.capture());

        Assert.assertEquals(2, argument.getValue().size());
        Assert.assertEquals(list, argument.getValue());
    }
}


