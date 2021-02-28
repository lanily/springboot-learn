## AOP 切面统一打印请求日志

本节中，您将学习如何在 Spring Boot 中使用 AOP 切面统一处理请求日志，打印进出参相关参数。

### 一、先看看日志输出效果
`Spring Boot AOP` 打印日志
可以看到，每个对于每个请求，开始与结束一目了然，并且打印了以下参数：

`URL`: 请求接口地址；
`HTTP Method`: 请求的方法，是 POST, GET, 还是 DELETE 等；
`Class Method`: 对应 Controller 的全路径以及调用的哪个方法;
`IP`: 请求 IP 地址；
`Request Args`: 请求入参，以 JSON 格式输出；
`Response Args`: 响应出参，以 JSON 格式输出；
`Time-Consuming`: 请求耗时；

效果应该还不错吧！接下来就让我们一步一步去实现该功能, 首先，新建一个 Spring Boot Web 项目。

### 二、添加 Maven 依赖
在项目 pom.xml 文件中添加依赖：
```xml
<!-- aop 依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

```xml
<!-- 用于日志切面中，以 json 格式打印出入参（本来使用阿里的 FASTJSON, 但是对于文件上传的接口，打印参数会报错，换为 Gson） -->

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.5</version>
</dependency>
```

### 三、配置 AOP 切面
在配置 AOP 切面之前，我们需要了解下 aspectj 相关注解的作用：

`@Aspect`：声明该类为一个注解类；
`@Pointcut`：定义一个切点，后面跟随一个表达式，表达式可以定义为某个 package 下的方法，也可以是自定义注解等；
切点定义好后，就是围绕这个切点做文章了：

`@Before`: 在切点之前，织入相关代码；
`@After`: 在切点之后，织入相关代码;
`@AfterReturning`: 在切点返回内容后，织入相关代码，一般用于对返回值做些加工处理的场景；
`@AfterThrowing`: 用来处理当织入的代码抛出异常后的逻辑处理;
`@Around`: 在切入点前后织入代码，并且可以自由的控制何时执行切点；


[来自于](https://juejin.cn/post/6844903779863625735)

## Spring AOP 实现简单的日志切面

- [SpringAOP](https://yq.aliyun.com/articles/723070)

## AOP

### 1.什么是 AOP ？

AOP 的全称为 Aspect Oriented Programming，译为面向切面编程，是通过预编译方式和运行期动态代理实现核心业务逻辑之外的横切行为的统一维护的一种技术。AOP 是面向对象编程（OOP）的补充和扩展。
利用 AOP 可以对业务逻辑各部分进行隔离，从而达到降低模块之间的耦合度，并将那些影响多个类的公共行为封装到一个可重用模块，从而到达提高程序的复用性，同时提高了开发效率，提高了系统的可操作性和可维护性。

### 2.为什么要用 AOP ？

在实际的 Web 项目开发中，我们常常需要对各个层面实现日志记录，性能统计，安全控制，事务处理，异常处理等等功能。如果我们对每个层面的每个类都独立编写这部分代码，那久而久之代码将变得很难维护，所以我们把这些功能从业务逻辑代码中分离出来，聚合在一起维护，而且我们能灵活地选择何处需要使用这些代码。

### 3.AOP 的核心概念

| 名词                      | 概念                                                         | 理解                                                         |
| :------------------------ | :----------------------------------------------------------- | :----------------------------------------------------------- |
| 通知（Advice）            | 拦截到连接点之后所要执行的代码，通知分为前置、后置、异常、最终、环绕通知五类 | 我们要实现的功能，如日志记录，性能统计，安全控制，事务处理，异常处理等等，说明什么时候要干什么 |
| 连接点（Joint Point）     | 被拦截到的点，如被拦截的方法、对类成员的访问以及异常处理程序块的执行等等，自身还能嵌套其他的 Joint Point | Spring 允许你用通知的地方，方法有关的前前后后（包括抛出异常） |
| 切入点（Pointcut）        | 对连接点进行拦截的定义                                       | 指定通知到哪个方法，说明在哪干                               |
| 切面（Aspect）            | 切面类的定义，里面包含了切入点（Pointcut）和通知（Advice）的定义 | 切面就是通知和切入点的结合                                   |
| 目标对象（Target Object） | 切入点选择的对象，也就是需要被通知的对象；由于 Spring AOP 通过代理模式实现，所以该对象永远是被代理对象 | 业务逻辑本身                                                 |
| 织入（Weaving）           | 把切面应用到目标对象从而创建出 AOP 代理对象的过程。织入可以在编译期、类装载期、运行期进行，而 Spring 采用在运行期完成 | 切点定义了哪些连接点会得到通知                               |
| 引入（Introduction ）     | 可以在运行期为类动态添加方法和字段，Spring 允许引入新的接口到所有目标对象 | 引入就是在一个接口/类的基础上引入新的接口增强功能            |
| AOP 代理（AOP Proxy ）    | Spring AOP 可以使用 JDK 动态代理或者 CGLIB 代理，前者基于接口，后者基于类 | 通过代理来对目标对象应用切面                                 |

## Spring AOP

### 1.简介

AOP 是 Spring 框架中的一个核心内容。在 Spring 中，AOP 代理可以用 JDK 动态代理或者 CGLIB 代理 CglibAopProxy 实现。Spring 中 AOP 代理由 Spring 的 IOC 容器负责生成和管理，其依赖关系也由 IOC 容器负责管理。

### 2.相关注解

| 注解            | 说明                                                         |
| :-------------- | :----------------------------------------------------------- |
| @Aspect         | 将一个 java 类定义为切面类                                   |
| @Pointcut       | 定义一个切入点，可以是一个规则表达式，比如下例中某个 package 下的所有函数，也可以是一个注解等 |
| @Before         | 在切入点开始处切入内容                                       |
| @After          | 在切入点结尾处切入内容                                       |
| @AfterReturning | 在切入点 return 内容之后处理逻辑                             |
| @Around         | 在切入点前后切入内容，并自己控制何时执行切入点自身的内容     |
| @AfterThrowing  | 用来处理当切入内容部分抛出异常之后的处理逻辑                 |
| @Order(100)     | AOP 切面执行顺序， @Before 数值越小越先执行，@After 和 @AfterReturning 数值越大越先执行 |

其中 @Before、@After、@AfterReturning、@Around、@AfterThrowing 都属于通知（Advice）。

## 利用 AOP 实现 Web 日志处理

### 1.构建项目

### 2.添加依赖

```
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- 热部署模块 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional> <!-- 这个需要为 true 热部署才有效 -->
        </dependency>
        <!-- Spring AOP -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
    </dependencies>
```

### 3.Web 日志注解

```
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControllerWebLog {
     String name();//所调用接口的名称
     boolean intoDb() default false;//标识该条操作日志是否需要持久化存储
}
```

### 4.实现切面逻辑

```
@Aspect
@Component
@Order(100)
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
```
