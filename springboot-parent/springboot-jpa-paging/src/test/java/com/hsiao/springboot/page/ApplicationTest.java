package com.hsiao.springboot.page;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *  MOCK—提供一个Mock的Servlet环境，内置的Servlet容器并没有真实的启动，主要搭配使用@AutoConfigureMockMvc
 *
 * RANDOM_PORT提供一个真实的Servlet环境，也就是说会启动内置容器，然后使用的是随机端口
 *  DEFINED_PORT这个配置也是提供一个真实的Servlet环境，使用的默认的端口，如果没有配置就是8080
 *  NONE这是个神奇的配置，跟Mock一样也不提供真实的Servlet环境。
 *
 * @RunWith 是junit提供的注解，表示该类是单元测试的执行类
 * SpringRunner是spring-test提供的测试执行单元类(是Spring单元测试中SpringJUnit4ClassRunner的新名字)
 * SpringBootTest 是执行测试程序的引导类
 *
 * @projectName springboot-parent
 * @title: ApplicationTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/30
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PageApplication.class)
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class ApplicationTest {


}


