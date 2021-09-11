package com.hsiao.springboot.mockito;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.hsiao.springboot.mockito.entity.Bar;
import com.hsiao.springboot.mockito.entity.Foo;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * 使用Mockito.spy()方法
 * spy对象也是基于mock对象生成的，或者说spy对象和mock对象是可以互相转换的
 *
 * @Spy
 *
 * Mockito中的Mock和Spy都可用于拦截那些尚未实现或不期望被真实调用的对象和方法，并为其设置自定义行为。
 * 二者的区别在于：
 *
 * 1、Mock声明的对象，对函数的调用均执行mock（即虚假函数），不执行真正部分。
 *
 * 2、Spy声明的对象，对函数的调用均执行真正部分。
 *
 * 请注意，spy对象与mock对象的区别：
 *
 *
 * 默认的返回策略不同：
 *
 * mock对象的默认返回类型的空值（可以配置返回策略），不执行实际方法。
 * spy对象是默认执行实际方法并返回，可以对spy对象的某个方法进行存根以指定返回值且避免调用此方法实际逻辑。
 *
 *
 *
 * 存根的方式不同：
 *
 * 存根的方式有两种：
 *
 * then方式：Mockito.when(foo.sum()).thenXXX(...);
 * do方式：Mockito.doXXX(...).when(foo).sum();
 *
 *
 *
 * 对于mock对象，两种方式都可以使用。但是void方法必须使用do方式的存根。
 *
 *
 * 对于spy对象，只能使用do方式的存根（因为使用then方式会使得被存根方法的实际代码）。
 *
 * 最佳实践：对方法的存根只是用do式而不是then方式。
 *
 * Mockito 不会将调用传递给的真实实例，实际上是时创建了它的副本。因此，如果您保留真实实例并与之交互，则不要指望监视的人会知道这些交互及其对真实实例状态的影响。相应的，当unstubbed（没有进行存根）的方法在spy对象上调用但不在真实实例上调用时，您将看不到对真实实例的任何影响。
 *
 * 意思就是spy对象其实是真实对象的一个复制体；对spy中的方法的调用不会影响到真实对象。
 *
 * 注意 final方法。Mockito 不会 mock final方法，所以底线是：当您spy一个真实对象时（使用spy()方法） + 尝试存根 final方法 = 麻烦。您也将无法验证这些方法。
 * 像往常一样，您将看到部分mock的警告：面向对象编程通过将复杂性划分为单独的、特定的 SRPy 对象来减少复杂性。部分部分mock如何适应这种范式？嗯，它只是没有......部分mock通常意味着复杂性已转移到同一对象上的不同方法。在大多数情况下，这不是您想要设计应用程序的方式。
 * 但是，在极少数情况下，部分mock会派上用场：处理您无法轻松更改的代码（第 3 方接口、遗留代码的临时重构等）但是，我不会将部分mock用于新的、测试驱动的 & 良好设计的代码。

 *
 * @projectName springboot-parent
 * @title: MockitoSpyTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)

public class MockitoSpyTest {

    //foo 对象内部的成员变量会自动被 @Mock 注解的生成的对象注入。
    @InjectMocks
    private Foo foo;

    //bar 对象会自动的注入到 @InjectMocks 注解的对象的成员变量中去。
    @Spy
    private Bar bar;

    //也可以这样
    @Spy
    private Bar bar2 = new Bar();

    @Test
    public void mockTest() {
        //先对spy对象的待测方法进行存根
        doReturn(7).when(bar).add(1, 2);

        int result = foo.sum(1, 2);
        //验证是否是存根返回的值
        Assert.assertEquals(7, result);
    }

    @Test
    public void mockTest2() {
        //不对spy对象存根，则调用实际的方法。
        //注意同mock对象的区别：mock对象默认是返回类型的空值。而spy对象是默认执行实际方法并返回。
        int result = foo.sum(1, 2);
        Assert.assertEquals(3, result);
    }


    @Test
    public void testSpy() {
        List list = new LinkedList();
        List spy = spy(list);

        //方式1：spy一个具体的类型
//        List spy1 = Mockito.spy(List.class);
//方式2：spy一个已存在的对象
//        List spy2 = Mockito.spy(new ArrayList<>());

//以下代码是不合适的: spy.get(0)方法实际的代码会被调用， 会抛出
//IndexOutOfBoundsException (list仍然是空的)。

//为什么在存根的时候就会调用方法实际的代码？这样不就无法对此方法进行存根了吗？

//因为在执行when(spy.get(0))的时候首先执行的是when()方法内的spy.get(0)；
//而此时spy.get(0)还没有进行存根。故此方法的实际代码会被调用。

//要解决这个问题，请使用doXXX()方法配合when()进行存根。
        when(spy.get(0)).thenReturn("foo");

//你需要用doReturn()去存根
        doReturn("foo").when(spy).get(0);

    }
}

