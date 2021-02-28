package com.hsiao.springboot.ehcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 在项目里启用缓存，有注解和 XML 配置两种方式
 *
 * <p>使用 @EnableCaching 注解 //
 * com.hsiao.springboot.ehcache.SpringbootEhcacheApplication.java
 *
 * @EnableCaching @SpringBootApplication
 * public class SpringbootEhcacheApplication { public static void main(String[] args) {
 * SpringApplication.run(SpringbootEhcacheApplication.class, args); } } 复制代码
 *
 * <p>或者在 Spring 的 XML 文件中添加
 * <cache:annotation-driven />
 * <beans
 * xmlns="http://www.springframework.org/schema/beans"
 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * xmlns:cache="http://www.springframework.org/schema/cache"
 * xsi:schemaLocation="http://www.springframework.org/schema/beans
 * http://www.springframework.org/schema/beans/spring-beans.xsd
 * http://www.springframework.org/schema/cache
 * http://www.springframework.org/schema/cache/spring-cache.xsd">
 *
 * <cache:annotation-driven />
 * </beans>
 *

 */
@EnableCaching
@SpringBootApplication
public class SpringBootEhcache2Application {
  public static void main(String[] args) {
    SpringApplication.run(SpringBootEhcache2Application.class, args);
  }
}
