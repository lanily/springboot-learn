### 一、Spring Boot 自述

世界上最好的文档来源自官方的《Spring Boot Reference Guide》，是这样介绍的：

>Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can “just run”…Most Spring Boot applications need very little Spring configuration.

`Spring Boot`(英文中是“引导”的意思)，是用来简化`Spring`应用的搭建到开发的过程。应用开箱即用，只要通过 “just run”（可能是 `java -jar` 或 `tomcat` 或 `maven`插件`run` 或 `shell`脚本），就可以启动项目。二者，`Spring Boot` 只要很少的`Spring`配置文件（例如那些`xml`，`property`）。

因为“习惯优先于配置”的原则，使得Spring Boot在快速开发应用和微服务架构实践中得到广泛应用。

 

`Javaer`装好`JDK`环境和`Maven`工具就可以开始学习`Spring Boot`了~

### 二、HelloWorld实战详解
首先得有个`maven`基础项目，可以直接使用`Maven`骨架工程生成`Maven`骨架`Web`项目，即`mvn archetype:generate`命令：

`mvn archetype:generate -DgroupId=springboot -DartifactId=springboot-helloworld -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`

#### 2.1  `pom.xml`配置
代码如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>springboot</groupId>
    <artifactId>springboot-helloworld</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springboot-helloworld :: HelloWorld Demo</name>

    <!-- Spring Boot 启动父依赖 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.3.RELEASE</version>
    </parent>

    <dependencies>
        <!-- Spring Boot web依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
</project>
```

只要加入一个 `Spring Boot` 启动父依赖即可。

 

#### 2.2 `Controller`层

HelloWorldController的代码如下：

```
/**
 * Spring Boot HelloWorld案例
 *
 * Created by hsiao on 18/4/26.
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String sayHello() {
        return "Hello,World!";
    }
}
```

`@RestController`和`@RequestMapping`注解是来自`SpringMVC`的注解，它们不是`SpringBoot`的特定部分。

`@RestController`：提供实现了`REST API`，可以服务JSON,XML或者其他。这里是以`String`的形式渲染出结果。
`@RequestMapping`：提供路由信息，”/“路径的HTTP Request都会被映射到`sayHello`方法进行处理。

具体参考，世界上最好的文档来源自官方的`《Spring Framework Document》`

#### 2.3 启动应用类
和第一段描述一样，开箱即用。如下面`Application`类：
```
/**
 * Spring Boot应用启动类
 *
 * Created by hsiao on 18/4/26.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
```

`@SpringBootApplication：Spring Boot` 应用的标识
`Application`很简单，一个`main`函数作为主入口。`SpringApplication`引导应用，并将`Application`本身作为参数传递给`run`方法。具体`run`方法会启动嵌入式的`Tomcat`并初始化`Spring`环境及其各`Spring`组件。

 
#### 2.4 Controller层测试类
一个好的程序，不能缺少好的UT。针对HelloWorldController的UT如下：

```
/**
 * Spring Boot HelloWorldController 测试 - {@link HelloWorldController}
 *
 * Created by hsiao on 18/4/26.
 */
public class HelloWorldControllerTest {

    @Test
    public void testSayHello() {
        assertEquals("Hello,World!",new HelloWorldController().sayHello());
    }
}
```

### 三、运行
`Just Run`的宗旨，运行很简单，直接右键`Run`运行`Application`类。同样你也可以`Debug Run`。可以在控制台中看到：

`Tomcat started on port(s): 8080 (http)`
`Started Application in 5.986 seconds (JVM running for 7.398)`
然后访问 `http://localhost:8080/` ,即可在页面中看到`Spring Boot`对你 `say hello`：

Hello,World！
 

### 四、小结
* Spring Boot pom配置
* Spring Boot 启动及原理