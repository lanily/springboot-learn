package com.hsiao.springboot.mockito;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.hsiao.springboot.mockito.entity.Employee;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.verification.NoInteractionsWanted;
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

    /**
     * 验证行为
     * 没返回值的方法匹配
     */
    @Test
    public void test_thenNoReturn() {
        //模拟创建一个List对象
        List mock = mock(List.class);
        //使用mock的对象
        mock.add(1); // 基础类型
        mock.add(Lists.newArrayList("a")); // 引用类型
        mock.clear();
        verify(mock).add(1);
        verify(mock).add(Lists.newArrayList("a"));
        verify(mock).clear();
    }


    /**
     * 模拟我们所期望的结果
     *
     */

    @Test
    public void when_thenReturn() {
        //mock一个Iterator类
        Iterator iterator = mock(Iterator.class);
        //预设当iterator调用next()时第一次返回hello，第n次都返回world
        when(iterator.next()).thenReturn("hello").thenReturn("world");
        //使用mock的对象
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
        //验证结果
        assertEquals("hello world world", result);

        //  创建 mock
        Employee test = mock(Employee.class);
        // 自定义 getId() 的返回值
        when(test.getId()).thenReturn(2L);
        // 在测试中使用mock对象
        assertEquals(Long.valueOf(2L), test.getId());
    }

    /**
     * 自定义返回结果
     */
    @Test
    public void test_thenReturnAnswer() {
        Map mockMap = mock(Map.class);
        Answer answer = (invocation) -> {
            Object[] args = invocation.getArguments();
            return String.valueOf(args[0]) + 110;
        };
        when(mockMap.get(anyInt())).then(answer);
        TestCase.assertEquals("120110", mockMap.get(120));
    }


    /**
     * 设置抛出异常
     */
    @Test
    public void test_thenThrowException() {
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

    /**
     * 返回多个值
     */
    @Test
    public void test_thenMoreThanOneReturnValue() {
        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Mockito").thenReturn("rocks");
        String result = i.next() + " " + i.next();
        // 断言
        assertEquals("Mockito rocks", result);
    }

    /**
     * 如何根据输入来返回值
     */
    @Test
    public void test_thenReturnValueDependentOnMethodParameter() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo("Mockito")).thenReturn(1);
        when(c.compareTo("Eclipse")).thenReturn(2);
        // 断言
        assertEquals(1, c.compareTo("Mockito"));
    }

    /**
     * 如何让返回值不依赖于输入
     */
    @Test
    public void test_thenReturnValueInDependentOnMethodParameter() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo(anyInt())).thenReturn(-1);
        // 断言
        assertEquals(-1, c.compareTo(9));
    }

    /**
     * 连续的方法调用设置不同的行为
     */
    @Test
    public void test_thenWithDifference() {
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

    /**
     * 测试方法的调用次数
     */
    @Test
    public void test_withTimes() {
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
     *RETURNS_SMART_NULLS和RETURNS_DEEP_STUBS
     * RETURNS_SMART_NULLS实现了Answer接口的对象，它是创建mock对象时的一个可选参数，mock(Class,Answer)。
     *
     * 在创建mock对象时，有的方法我们没有进行stubbing，所以调用时会放回Null这样在进行操作是很可能抛出NullPointerException。如果通过RETURNS_SMART_NULLS参数创建的mock对象在没有调用stubbed方法时会返回SmartNull。例如：返回类型是String，会返回"";是int，会返回0；是List，会返回空的List。另外，在控制台窗口中可以看到SmartNull的友好提示
     */
    @Test
    public void test_returnsSmartNullsTest() {
        List mock = mock(List.class, RETURNS_SMART_NULLS);
        System.out.println(mock.get(0));

        //使用RETURNS_SMART_NULLS参数创建的mock对象，不会抛出NullPointerException异常。另外控制台窗口会提示信息“SmartNull returned by unstubbed get() method on mock”
        System.out.println(mock.toArray().length);
    }

    /**
     * 配置 Mock 对象
     * 当我们有了一个 Mock 对象后, 我们可以定制它的具体的行为.
     */
    @Test
    public void createMockObject() {
        // 使用 mock 静态方法创建 Mock 对象.
        List mockedList = mock(List.class);
        assertTrue(mockedList instanceof List);

        // mock 方法不仅可以 Mock 接口类, 还可以 Mock 具体的类型.
        ArrayList mockedArrayList = mock(ArrayList.class);
        assertTrue(mockedArrayList instanceof List);
        assertTrue(mockedArrayList instanceof ArrayList);
    }

    @Test
    public void configMock() {
        Map map = mock(Map.class);
        //当调用map.size()方法时候，返回100
        when(map.size()).thenReturn(100);
        Assert.assertEquals(map.size(), 100);
        //当调用map.put(1，2)方法时候，返回true，参数要匹配
        when(map.put(1, 2)).thenReturn(true);
        assertTrue((Boolean) map.put(1, 2));
        //当调用map.get(1)方法时候，抛空指针
        doThrow(new NullPointerException()).when(map).get(1);
        //表示调用size()方法什么都不做，个人觉得可以用在依赖组件或者依赖方法返回void的情况下
        doNothing().when(map).size();
        map.size();
        verify(map, times(1)).size();  //verify检测方法调用，这表明size方法调用一次

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
     * Mock不是真实的对象，它只是用类型的class创建了一个虚拟对象，并可以设置对象行为
     * Spy是一个真实的对象，但它可以设置对象行为
     * InjectMocks创建这个类的对象并自动将标记@Mock、@Spy等注解的属性值注入到这个中
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
        // 因为我们没有对 get(0), get(1) 方法进行定制,所有这两个调用的是真实方法
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
        //捕获参数的对象
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        //mock对象执行add方法，将list添加进其中
        mockedList.addAll(list);
        //捕获参数
        verify(mockedList).addAll(argument.capture());
        //断言验证
        Assert.assertEquals(2, argument.getValue().size());
        Assert.assertEquals(list, argument.getValue());
    }

    /**
     * 除了匹配制定参数外，还可以匹配自己想要的任意参数
     */
    @Test
    public void with_unspecified_arguments() {
        List list = mock(List.class);
        //匹配任意参数
        when(list.get(anyInt())).thenReturn(1);
        when(list.contains(argThat(new IsValid()))).thenReturn(true);
        assertEquals(1, list.get(1));
        assertEquals(1, list.get(999));
        assertTrue(list.contains(1));
        assertTrue(!list.contains(3));
    }

    private class IsValid implements ArgumentMatcher<Integer> {

        @Override
        public boolean matches(Integer o) {
            return o == 1 || o == 2;
        }
    }

    /**
     * 注意：如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
     */
    @Test
    public void all_arguments_provided_by_matchers() {
        Comparator comparator = mock(Comparator.class);
        comparator.compare("nihao", "hello");
        //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
        verify(comparator).compare(anyString(), eq("hello"));
        //下面的为无效的参数匹配使用
        //verify(comparator).compare(anyString(),"hello");
    }

    /**
     *验证执行顺序
     */
    @Test
    public void test_verifyInOrder() {
        List list = mock(List.class);
        List list2 = mock(List.class);
        list.add(1);
        list2.add("hello");
        list.add(2);
        list2.add("world");
        //将需要排序的mock对象放入InOrder
        InOrder inOrder = inOrder(list, list2);
        //下面的代码不能颠倒顺序，验证执行顺序
        inOrder.verify(list).add(1);
        inOrder.verify(list2).add("hello");
        inOrder.verify(list).add(2);
        inOrder.verify(list2).add("world");
    }

    /**
     *确保模拟对象上无互动发生
     */
    @Test
    public void test_verifyInteraction() {
        List list = mock(List.class);
        List list2 = mock(List.class);
        List list3 = mock(List.class);
        list.add(1);
        verify(list).add(1);
        verify(list, never()).add(2);
        //验证零互动行为
        verifyZeroInteractions(list2, list3);
    }

    /**
     * 找出冗余的互动(即未被验证到的)
     */
    @Test(expected = NoInteractionsWanted.class)
    public void test_findRedundantInteraction() {
        List list = mock(List.class);
        list.add(1);
        list.add(2);
        verify(list, times(2)).add(anyInt());
        //检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
        verifyNoMoreInteractions(list);

        List list2 = mock(List.class);
        list2.add(1);
        list2.add(2);
        verify(list2).add(1);
        //检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
        verifyNoMoreInteractions(list2);
    }
}


