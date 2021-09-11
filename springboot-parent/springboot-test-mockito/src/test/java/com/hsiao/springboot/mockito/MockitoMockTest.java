package com.hsiao.springboot.mockito;


import com.hsiao.springboot.mockito.entity.Bar;
import com.hsiao.springboot.mockito.entity.Foo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *测试中我们创建的对象一般可以分为三种：被测对象、mock对象和spy对象。
 * 首先我们明确一下这三种对象的概念：
 *
 * 被测对象：即我们想要测试的对象，比如xxService、xxUtils等。
 * mock对象：一般为我们被测对象的依赖对象。典型如被测对象的成员变量。主要是一些测试中我们不关注的对象。我们只想要得到这些对象的方法的返回值。而不关注这些方法的具体执行逻辑。此时我们可以将这些对象创建为mock对象。
 * spy对象：在Mockito中它是基于部分mock概念提出的。spy对象也可由mock对象使用特定参数下创建。也就是说：**spy对象其实是一种特殊的mock对象。**和mock对象一样，它可以作为被测对象的依赖对象。此时它和mock对象的最大的区别是mock对象的方法如果没有被存根，调用时会返回相应对象的空值（下文有详细介绍）；而spy对象的方法被调用时则会调用真实的代码逻辑。
 *
 *
 * 链接：https://juejin.cn/post/6976306102774267935
 *
 * @InjectMocks的作用：标记应执行注入的字段（指此对象内的字段应该被自动的注入，注入的值来自@Mock或@Spy注解的字段）。
 *
 * 允许快速的mock和spy注入。
 * 最大限度地减少重复的mock和spy注入代码。
 *
 * 使用@InjectMocks注解时，配合使用@RunWith(MockitoJUnitRunner.class)或在@Before方法中使用MockitoAnnotations.openMocks(this)即可激活Mockito对注入相关的支持。
 * @Mock注解的字段会被自动注入到@InjectMocks注解生成的对象的成员变量中。
 * @Spy注解的字段会被自动注入到@InjectMocks注解生成的对象的成员变量中。
 *
 * 使用@Mock注解
 *
 * 优点：
 *
 * 最大限度地减少重复的mock创建代码。
 * 使测试类更具可读性。
 * 使验证错误更易于阅读，因为字段名称会被用于标识mock。
 *
 * 注意事项:
 *
 * 默认情况下，对于所有方法的返回值，mock 对象视情况而定将返回 null、原始/原始包装值或空集合。例如 int/Integer 返回0，布尔值/布尔值返回false。我们可以在创建mock时通过指定默认的Answer策略来更改此返回。支持的Answer策略请查看下文。
 *
 * @projectName springboot-parent
 * @title: MockitoTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoMockTest {

    //foo 对象内部的成员变量会自动被 @Mock 注解的生成的对象注入。
    @InjectMocks
    private Foo foo;

    //bar 对象会自动的注入到 @InjectMocks 注解的对象的成员变量中去。
    @Mock
    private Bar bar;

    @Test
    public void mockTest() {
        //先对mock对象的待测方法进行存根，当真正执行到mock对象的此方法时
        //会直接返回存根的结果而不会调用mock对象的实际代码
        Mockito.when(bar.add(1, 2)).thenReturn(7);

        int result = foo.sum(1, 2);
        //验证是否是存根返回的值
        Assert.assertEquals(7, result);
    }

}


