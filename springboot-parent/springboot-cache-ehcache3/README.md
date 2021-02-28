# Spring Boot 快速集成 Ehcache3

### 前言
在互联网服务端架构中，缓存的作用是尤为重要的，无论是基于服务器的内存缓存如 Redis，还是 基于 JVM 的内存缓存如 Ehcache ，在高并发场景中承载着巨大的流量，本文主要针对  JVM 内存框架 Ehcache 3 进行简单地练习，基于Spring Boot 集成 Ehcache 3 搭建一个简单的项目，来实现程序的内存缓存功能支持。

### 正文
`Ehcache 3`
Ehcache 是一个开源，具有高性能的 Java 缓存库，由于使用简单，扩展性强，是使用最广泛的 Java 缓存框架，同时具备了内存缓存和磁盘缓存的能力，最新的版本是 Ehcache 3.6。

### 集成步骤


首先创建一个基本的 Spring Boot 程序取名为 `springboot-jpa-ehcache3`，（版本为 2.1.8，以 maven 作为构建工具，不选择任何依赖。（本项目采用 IDEA 2018.5）


在项目的 `pom.xml` 里添加 `Ehcache 3` 依赖，选择合适的版本，这里采用了3.0.0。

`pom.xml`

```xml
<dependency>
  <groupId>org.ehcache</groupId>
  <artifactId>ehcache</artifactId>
  <version>3.0.0</version>    
 </dependency>
```




在项目的 `pom.xml` 里添加 `JSR-107 API` 依赖

关于  `JSR-107 API：Java` 缓存规范的文档 `API`，类似 `JDBC` 规范。
`pom.xml`
```xml
<dependency>
    <groupId>javax.cache</groupId>
    <artifactId>cache-api</artifactId>
</dependency>
```



添加 Spring Boot 依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId> 
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId> 
    </dependency>
</dependencies>

```


前两个依赖是 Spring Boot 程序 创建时默认有的，这里的 `spring-boot-starter-cache` 就是使用 Spring 框架的缓存功能，而加入了 `spring-boot-starter-web` 主要为了引入 Spring MVC，方便测试缓存的效果。



在程序配置文件 `application.properties` 中指定 `ehcache.xml` 的路径,一般放置在当前 `classpath `下；这样就让 Spring 缓存启用 Ehcache。
`application.properties`

`spring.cache.jcache.config=classpath:ehcache.xml`


在项目里启用缓存，有注解和 XML 配置两种方式

使用  `@EnableCaching` 注解

```java
// com.one.springbootehcache2.SpringbootEhcacheApplication.java 
@EnableCaching
@SpringBootApplication
public class SpringbootEhcacheApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpringbootEhcacheApplication.class, args);
    }
}
```

或者在 Spring 的 `XML` 文件中添加
```xml
<cache:annotation-driven />
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:cache="http://www.springframework.org/schema/cache"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd">

    <cache:annotation-driven />
</beans>
```





在需要使用缓存的方法上使用注解 `@CacheResult` 进行声明，这样一旦调用这个方法，返回的结果就会被缓存，除非缓存被清除掉，下次就不会执行方法的逻辑了。
`PersonService.java`

```java
// com.hsiao.springboot.ehcache.service.PersonService.java
@Service
public class PersonService {
    @CacheResult(cacheName="people")
    Person getPerson(int id) {
        System.out.println("未从缓存读取 " + id);
        switch (id) {
            case 1:
                return new Person(id, "Steve", "jobs");
            case 2:
                return new Person(id, "bill", "gates");
            default:
                return new Person(id, "unknown", "unknown");
        }
    }
}
```


`Person.java`
```java
// com.hsiao.springboot.ehcache.domain.Person.java 
public class Person implements Serializable {
        private int id;
        private String firstName;
        private String lastName;

        public Person(int id, String firstName, String lastName) {
            this.id =id;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
}
```

`@CacheResult` 必须指定 `cacheName`，否则 `cacheName` 默认视为该方法名称。



在 `ehcache.xml`  配置基本缓存参数
`ehcache.xml`
```xml
<config
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
    xmlns='http://www.ehcache.org/v3'  
    xmlns:jsr107='http://www.ehcache.org/v3/jsr107'>  

  <service>
    <jsr107:defaults>
      <jsr107:cache name="people" template="heap-cache"/> 
    </jsr107:defaults>
  </service>

  <cache-template name="heap-cache">
    <listeners>    
      <listener>
        <class>com.one.springbootehcache.config.EventLogger</class>
        <event-firing-mode>ASYNCHRONOUS</event-firing-mode>
        <event-ordering-mode>UNORDERED</event-ordering-mode>
        <events-to-fire-on>CREATED</events-to-fire-on> 
        <events-to-fire-on>UPDATED</events-to-fire-on> 
        <events-to-fire-on>EXPIRED</events-to-fire-on> 
        <events-to-fire-on>REMOVED</events-to-fire-on> 
      </listener>
    </listeners>
    <resources>
      <heap unit="entries">2000</heap> 
      <offheap unit="MB">100</offheap> 
    </resources>
  </cache-template>
</config>
```


声明一个名为 `people` 的缓存,指定 `heap-cache` 为模板


在缓存模板里配置了日志输入器 EventLogger，用来监听缓存数据变更的事件，例如数据创建，更新，失效等进行事件日志输出。
```java

//com.hsiao.springboot.ehcache.config.EventLogger.java
public class EventLogger implements CacheEventListener<Object, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogger.class);

    @Override
    public void onEvent(CacheEvent<Object, Object> event) {
        LOGGER.info("Event: " + event.getType() + " Key: " + event.getKey() + " old value: " + event.getOldValue() + " new value: " + event.getNewValue());
    }

}
```


对 `CREATED`，`UPDATED`，`EXPIRED`，`REMOVED` 这四个事件进行监听。


最后的 `resources` 元素配置了缓存能容纳的最大对象个数为2000，堆外内存容量为100M。


实现  `JCacheManagerCustomizer.customize(CacheManager cacheManager)`  方法在 `CacheManager` 使用之前，创建我们配置文件定义的缓存,并声明了缓存策略为10秒。
```java
// com.hsiao.springboot.ehcache.config.CachingSetup.java
@Component
public  class CachingSetup implements JCacheManagerCustomizer {
    @Override
    public void customize(CacheManager cacheManager)
    {
      cacheManager.createCache("people", new MutableConfiguration<>()  
        .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(SECONDS, 10))) 
        .setStoreByValue(false)
        .setStatisticsEnabled(true));
    }
}

```




创建一个控制器 `PersonController`，进行缓存的测试。
```java
// com.hsiao.springboot.ehcache.domain.Person.java
@RequestMapping("/person")
@RestController
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @RequestMapping("/get")
    public Person getPerson(int id) {
        Person person = personService.getPerson(id);
        LOGGER.info("读取到数据 " + person.getFirstName() + "," + person.getLastName());
        return person;
    }
}
```
启动程序，快速两次访问 `http://localhost:8080/person/get?id=1`，可以从控制台看到如下结果：

可以看出第二次访问时，直接使用的先前缓存的数据。由于缓存过期策略设置为 10秒，过了10秒再访问一次查看日志，可以根据事件日志器看出缓存失效后重新获取的数据，再添加到缓存中去。



到这里，我们的 `Ehcache 3` 与 `Spring Boot` 集成整合就算完成了，虽然项目比较简单，但可以基于此参考更详细的 Ehcache 配置来进行扩展。
问题列表
下面是我搭建项目过程中踩到的坑，这里放出来，希望能对同样遇到问题的同学有所参考。

**问题一：实体类未实现 `java.io.Serializable` 接口 **
```java
2019-02-17 15:50:07.606 ERROR 29671 --- [nio-8080-exec-4] o.a.c.c.C.[.[.[/].[dispatcherServlet] : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.data.redis.serializer.SerializationException: Cannot serialize; nested exception is org.springframework.core.serializer.support.SerializationFailedException: Failed to serialize object using DefaultSerializer; nested exception is java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.one.springbootehcache.domain.Person]] with root cause 
java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.hsiao.springboot.ehcache.domain.Person]
```
 

解决办法：`Ehcahe` 需要缓存的实体类必须实现 `java.io.Serializable` 接口


问题二：注解 `@CacheResult` 未指定缓存名称



解决办法：`@CacheResult` 的 `cacheName` 必须指定配置创建的缓存 ，否则 `cacheName` 默认为该方法完全名称。


问题三：没有正确定义 事件日志器，导致 `cacheManager` 创建缓存出错
