package com.hsiao.springboot.async;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * 异步方法demo启动类
 *
 * 异步调用的概念
 * 异步调用相对于同步调用而言，通常的方法都是程序按照顺序来执行的，程序的每一步都需要等到上一步执行完成之后才能继续往下执行；而异步调用则无需等待，它可以在不阻塞主线程的情况下执行高耗时方法
 *
 * 如何实现异步调用
 * 在不使用SpringBoot的时候我们通常使用多线程的方式来实现异步调用，对于一个web项目的话就需要使用线程池来创建多线程进行调用，而SpringBoot提供了很方便的@Async注解来实现异步方法
 *
 * Spring中用@Async注解标记的方法，称为异步方法。在spring boot应用中使用@Async很简单：
 * 1、调用异步方法类上或者启动类加上注解@EnableAsync
 * 2、在需要被异步调用的方法外加上@Async
 * 3、所使用的@Async注解方法的类对象应该是Spring容器管理的bean对象；
 *
 * 启动类加上注解@EnableAsync：
 *
 * 需要使用到的注解：
 * @Async：用于指定注解的方式为异步调用的方法
 * @EnableAsync：用于开启异步调用机制，注解在有@Config注解修饰的类上
 *
 * 需要注意的问题一：异步方法的定义位置问题
 * 最好将异步调用的方法单独放在一个@Component类中，或者说不要将异步调用方法写在@Controller中，否则将无法进行调用，因为SpringBoot使用@Transaction需要经过事务拦截器，只有通过了该事务拦截器的方法才能被加入Spring的事务管理器中，而在同一个类中的一个方法调用另一个方法只会经过一次事务拦截器，所以如果是后面的方法使用了事务注解将不会生效，在这里异步调用也是同样的道理
 *
 *
 * 需要注意的问题二：异步方法的事务调用问题
 * 在@Async注解的方法上再使用@Transaction注解是无效的，在@Async注解的方法中调用Service层的事务方法是有效的
 *
 * 需要注意的问题三：异步方法必须是实例的
 * 因为静态方法不能被override重写，因为@Async异步方法的实现原理是通过注入一个代理类到Bean中，该代理类集成这个Bean并且需要重写这个异步方法，所以需要是实例方法
 * @projectName springboot-parent
 * @title: AsyncApplication
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@SpringBootApplication
//开启异步调用方法
//@EnableAsync // 开启异步支持, 一般放在配置类上
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }
}