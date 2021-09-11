package com.hsiao.springboot.test.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * 参数化测试 Junit 4 引入了一个新的功能参数化测试。
 * 参数化测试允许开发人员使用不同的值反复运行同一个测试。
 * 将遵循 5 个步骤来创建参数化测试。
 *
 * <p>
 * 用 @RunWith(Parameterized.class)来注释 test 类。
 * 创建一个由 @Parameters
 * 注释的公共的静态方法，它返回一个对象的集合(数组)
 * 来作为测试数据集合。
 * 创建一个公共的构造函数，它接受和一行测试数据相等同的东西。
 * 为每一列测试数据创建一个实例变量。
 * 用实例变量作为测试数据的来源来创建你的测试用例。
 *
 *
 * 参数化测试
 * @RunWith: 当类被@RunWith注释修饰, 或者类继承了一个被该注解类修饰的类, JUnit将会使用这个注解所指明的运行器(runner)来运行测试, 而不是JUni默认的运行器
 *
 * 要进行参数化测试，需要在类上面指定如下的运行器：
 * @RunWith (Parameterized.class)
 *
 * 然后，在提供数据的方法上加上一个@Parameters注解，这个方法必须是静态static的，并且返回一个集合Collection。
 *
 * JUnit4中参数化测试要点：
 * 1. 测试类必须由Parameterized测试运行器修饰
 * 2. 准备数据。数据的准备需要在一个方法中进行，该方法需要满足一定的要求：
 *     1）该方法必须由Parameters注解修饰
 *     2）该方法必须为public static的
 *     3）该方法必须返回Collection类型
 *     4）该方法的名字不做要求
 *     5）该方法没有参数
 * @projectName springboot-parent
 * @title: ParameterTest
 * @description: TODO
 * @author xiao
 * @create 2021/4/20
 * @since 1.0.0
 */
//1.更改默认的测试运行器为RunWith(Parameterized.class)
@RunWith(Parameterized.class)
public class ParameterTest {
    // 2.声明变量存放预期值和测试数据
    private String firstName;
    private String lastName;

    //3.声明一个返回值 为Collection的公共静态方法，并使用@Parameters进行修饰
    @Parameterized.Parameters //
    public static List<Object[]> param() {
        // 这里我给出两个测试用例
        return Arrays.asList(new Object[][]{{"Mike", "Black"}, {"Cilcln", "Smith"}});
    }

    //4.为测试类声明一个带有参数的公共构造函数，并在其中为之声明变量赋值
    public ParameterTest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    // 5. 进行测试，发现它会将所有的测试用例测试一遍
    @Test
    public void test() {
        String name = firstName + " " + lastName;
        assertThat("Mike Black", is(name));
    }
}