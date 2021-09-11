package com.hsiao.springboot.test.service;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit中的注解 @BeforeClass：针对所有测试，只执行一次，且必须为static void
 * @Before：初始化方法，执行当前测试类的每个测试方法前执行。
 * @Test：测试方法，在这里可以测试期望异常和超时时间
 * @After：释放资源，执行当前测试类的每个测试方法后执行
 * @AfterClass：针对所有测试，只执行一次，且必须为static void
 * @Ignore：忽略的测试方法（只在测试类的时候生效，单独执行该测试方法无效）
 * @RunWith:可以更改测试运行器 ，缺省值 org.junit.runner.Runner
 *
 * <p>一个单元测试类执行顺序为： @BeforeClass –> @Before –> @Test –> @After –> @AfterClass
 *
 * <p>每一个测试方法的调用顺序为： @Before –> @Test –> @After.net/qq_35915384/article/details/80227297
 *
 * @projectName springboot-parent
 * @title: JunitTest
 * @description: TODO
 * @author xiao
 * @create 2021/4/20
 * @since 1.0.0
 */
public class JunitTest {
  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void say() {}

  /**
   * 超时测试
   * 如果一个测试用例比起指定的毫秒数花费了更多的时间，那么 Junit 将自动将它标记为失败。timeout
   * 参数和 @Test注释一起使用。现在让我们看看活动中的
   * @test(timeout)。 测试会失败，在一秒后会抛出异常
   * org.junit.runners.model.TestTimedOutException: test timed out after 1000 milliseconds
   */
  @Test(timeout = 1000)
  public void testTimeout() throws InterruptedException {
    TimeUnit.SECONDS.sleep(2);
    System.out.println("Complete");
  }

  /**
   * 异常测试
   * 测试代码是否它抛出了想要得到的异常。
   * expected 参数和 @Test 注释一起使用。
   * 现在让我们看看活动中的 @Test(expected)。
   *
   * 异常测试可以通过@Test(expected=Exception.class), 对可能发生的每种类型的异常进行测试
   *
   * 如果抛出了指定类型的异常, 测试成功
   * 如果没有抛出指定类型的异常, 或者抛出的异常类型不对, 测试失败
   */
  @Test(expected = NullPointerException.class)
  public void testNullException() {
    throw new NullPointerException();
  }
}
