## 间接实现static成员注入的N种方式

虽然Spring会忽略掉你直接使用**@Autowired + static成员**注入，但还是有很多方法来**绕过**这些限制，实现对静态变量注入值。下面A哥介绍2种方式，供以参考：

方式一：以set方法作为跳板，在里面实现对static静态成员的赋值

```java
@Component
public class UserHelper {

    static UCClient ucClient;

    @Autowired
    public void setUcClient(UCClient ucClient) {
        UserHelper.ucClient = ucClient;
    }
}
```

方式二：使用`@PostConstruct`注解，在里面为static静态成员赋值

```java
@Component
public class UserHelper {

    static UCClient ucClient;

    @Autowired
    ApplicationContext applicationContext;
    @PostConstruct
    public void init() {
        UserHelper.ucClient = applicationContext.getBean(UCClient.class);
    }
}
```

虽然称作是2种方式，但其实我认为思想只是一个：**延迟为static成员属性赋值**。因此，基于此思想**确切的说**会有N种实现方案（只需要保证你在使用它之前给其赋值上即可），各位可自行思考，A哥就没必要一一举例了。

------

### 高级实现方式

作为**福利**，A哥在这里提供一种更为高（zhuang）级（bi）的实现方式供以你学习和参考：

```
@Component
public class AutowireStaticSmartInitializingSingleton implements SmartInitializingSingleton {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    /**
     * 当所有的单例Bena初始化完成后，对static静态成员进行赋值
     */
    @Override
    public void afterSingletonsInstantiated() {
        // 因为是给static静态属性赋值，因此这里new一个实例做注入是可行的
        beanFactory.autowireBean(new UserHelper());
    }
}
```

UserHelper类**不再需要**标注`@Component`注解，也就是说它不再需要被Spirng容器管理（static工具类确实不需要交给容器管理嘛，毕竟我们不需要用到它的实例），这从某种程度上也是节约开销的表现。

```java
public class UserHelper {

    @Autowired
    static UCClient ucClient;
    ...
}
```

运行程序，结果输出：

```java
08:50:15.765 [main] INFO org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor - Autowired annotation is not supported on static fields: static cn.yourbatman.temp.component.UCClient cn.yourbatman.temp.component.UserHelper.ucClient
Exception in thread "main" java.lang.NullPointerException
    at cn.yourbatman.temp.component.UserHelper.getAndFilterTest(UserHelper.java:26)
    at cn.yourbatman.temp.component.RoomService.create(RoomService.java:26)
    at cn.yourbatman.temp.DemoTest.main(DemoTest.java:19)
```

报错。当然喽，这是我故意的，虽然抛异常了，但是看到我们的进步了没：**info日志只打印一句了**（自行想想啥原因哈）。不卖关子了，正确的姿势还得这么写：

```java
public class UserHelper {

    static UCClient ucClient;
    @Autowired
    public void setUcClient(UCClient ucClient) {
        UserHelper.ucClient = ucClient;
    }
}
```

再次运行程序，**一切正常**（info日志也不会输出喽）。这么处理的好处我觉得有如下三点：

1. 手动管理这种case的依赖注入，更可控。而非交给Spring容器去自动处理
2. 工具类**本身**并不需要加入到Spring容器内，这对于有大量这种case的话，是可以节约开销的
3. 略显高级，装x神器（可别小看装x，这是个中意词，你的加薪往往来来自于装x成功）

当然，你也可以这么玩：

```java
@Component
public class AutowireStaticSmartInitializingSingleton implements SmartInitializingSingleton {

    @Autowired
    private AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor;
    @Override
    public void afterSingletonsInstantiated() {
        autowiredAnnotationBeanPostProcessor.processInjection(new UserHelper());
    }
}
```

https://developer.aliyun.com/article/768082