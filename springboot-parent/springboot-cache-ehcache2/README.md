# SpringBoot中使用Ehcache的详细教程

`EhCache` 是一个纯 Java 的进程内缓存框架，具有快速、精干等特点，是 `Hibernate` 中默认的 `CacheProvider`。这篇文章主要介绍了SpringBoot中使用Ehcache的相关知识,需要的朋友可以参考下
`EhCache` 是一个纯 Java 的进程内缓存框架，具有快速、精干等特点，是 `Hibernate` 中默认的 CacheProvider`。用惯了 Redis，很多人可能已经忘记了还有 EhCache 这么一个缓存框架

## 一、简介

EhCache 是一个纯 Java 的进程内缓存框架，具有快速、精干等特点，是 Hibernate 中默认CacheProvider。Ehcache 是一种广泛使用的开源 Java 分布式缓存。主要面向通用缓存,Java EE 和轻量级容器。它具有内存和磁盘存储，缓存加载器,缓存扩展,缓存异常处理程序,一个 gzip 缓存 servlet 过滤器,支持 REST 和 SOAP api 等特点。

### 特性

+ 快速、简单
+ 多种缓存策略
+ 缓存数据有两级：内存和磁盘，因此无需担心容量问题
+ 缓存数据会在虚拟机重启的过程中写入磁盘
+ 可以通过RMI、可插入API等方式进行分布式缓存
+ 具有缓存和缓存管理器的侦听接口
+ 支持多缓存管理器实例，以及一个实例的多个缓存区域
+ 提供Hibernate的缓存实现

### 与 Redis 相比

`EhCache` 直接在jvm虚拟机中缓存，速度快，效率高；但是缓存共享麻烦，集群分布式应用不方便。
`Redis` 是通过 Socket 访问到缓存服务，效率比 EhCache 低，比数据库要快很多，处理集群和分布式缓存方便，有成熟的方案。如果是单个应用或者对缓存访问要求很高的应用，用 EhCache 。如果是大型系统，存在缓存共享、分布式部署、缓存内容很大的，建议用 Redis。

`EhCache` 也有缓存共享方案，不过是通过 `RMI` 或者 `Jgroup` 多播方式进行广播缓存通知更新，缓存共享复杂，维护不方便；简单的共享可以，但是涉及到缓存恢复，大数据缓存，则不合适。

## 二、引入 EhCache

### 1、引入依赖

在 pom.xml 文件中，引入 Ehcache 的依赖信息

```xml
<!-- ehcache依赖 -->
<dependency>
 <groupId>net.sf.ehcache</groupId>
 <artifactId>ehcache</artifactId>
 <version>2.10.6</version>
</dependency>
<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

```

### 2、配置文件

创建 EhCache 的配置文件：ehcache.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd">
 
 <!--
  磁盘存储:将缓存中暂时不使用的对象,转移到硬盘,类似于Windows系统的虚拟内存
  path:指定在硬盘上存储对象的路径
  path可以配置的目录有：
  user.home（用户的家目录）
  user.dir（用户当前的工作目录）
  java.io.tmpdir（默认的临时目录）
  ehcache.disk.store.dir（ehcache的配置目录）
  绝对路径（如：d:\\ehcache）
  查看路径方法：String tmpDir = System.getProperty("java.io.tmpdir");
  -->
 <diskStore path="java.io.tmpdir" />
 
 <!--
  defaultCache:默认的缓存配置信息,如果不加特殊说明,则所有对象按照此配置项处理
  maxElementsInMemory:设置了缓存的上限,最多存储多少个记录对象
  eternal:代表对象是否永不过期 (指定true则下面两项配置需为0无限期)
  timeToIdleSeconds:最大的发呆时间 /秒
  timeToLiveSeconds:最大的存活时间 /秒
  overflowToDisk:是否允许对象被写入到磁盘
  说明：下列配置自缓存建立起600秒(10分钟)有效 。
  在有效的600秒(10分钟)内，如果连续120秒(2分钟)未访问缓存，则缓存失效。
  就算有访问，也只会存活600秒。
  -->
 <defaultCache maxElementsInMemory="10000" eternal="false" timeToIdleSeconds="600"
     timeToLiveSeconds="600" overflowToDisk="true" />
 
 <!--
  maxElementsInMemory，内存缓存中最多可以存放的元素数量,若放入Cache中的元素超过这个数值,则有以下两种情况
       1)若overflowToDisk=true,则会将Cache中多出的元素放入磁盘文件中
       2)若overflowToDisk=false,则根据memoryStoreEvictionPolicy策略替换Cache中原有的元素
  eternal，   缓存中对象是否永久有效
  timeToIdleSeconds， 缓存数据在失效前的允许闲置时间(单位:秒)，仅当eternal=false时使用,默认值是0表示可闲置时间无穷大,若超过这个时间没有访问此Cache中的某个元素,那么此元素将被从Cache中清除
  timeToLiveSeconds， 缓存数据的总的存活时间（单位：秒），仅当eternal=false时使用，从创建开始计时，失效结束
  maxElementsOnDisk， 磁盘缓存中最多可以存放的元素数量,0表示无穷大
  overflowToDisk，  内存不足时,是否启用磁盘缓存
  diskExpiryThreadIntervalSeconds， 磁盘缓存的清理线程运行间隔,默认是120秒
  memoryStoreEvictionPolicy， 内存存储与释放策略,即达到maxElementsInMemory限制时,Ehcache会根据指定策略清理内存 共有三种策略,分别为LRU(最近最少使用)、LFU(最常用的)、FIFO(先进先出)
 -->
 <cache name="user"
    maxElementsInMemory="10000"
    eternal="false"
    timeToIdleSeconds="120"
    timeToLiveSeconds="120"
    maxElementsOnDisk="10000000"
    overflowToDisk="true"
    memoryStoreEvictionPolicy="LRU" />
 
</ehcache>
```

`<cache name="user"></cache>`，我们是可以配置多个来解决我们不同业务处所需要的缓存策略的

默认情况下，`EhCache` 的配置文件名是固定的，`ehcache.xml`，如果需要更改文件名，需要在 `application.yml` 文件中指定配置文件位置，如：
```yml

spring:
 cache:
 type: ehcache
 ehcache:
  config: classpath:/ehcache.xml
```

指定了 EhCache 的配置文件位置

### 3、开启缓存

开启缓存的方式，也和 Redis 中一样，在启动类上添加 `@EnableCaching` 注解即可：
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@SpringBootApplication
@EnableCaching
public class SbmApplication {
 
 public static void main(String[] args) {
  SpringApplication.run(SbmApplication.class, args);
 }
}

```

### 三、开始使用

1、`@CacheConfig`

这个注解在类上使用，用来描述该类中所有方法使用的缓存名称，当然也可以不使用该注解，直接在具体的缓存注解上配置名称，示例代码如下：
```java

@Service
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {
 
}

```

2、`@Cacheable`

这个注解一般加在查询方法上，表示将一个方法的返回值缓存起来，默认情况下，缓存的 key 就是方法的参数，缓存的 value 就是方法的返回值。示例代码如下：
```java

@Override
@Cacheable(value = "user", key = "#id")
public User getUserById(Long id) {
 return userMapper.getUserById(id);
}

```

如果在类上没有加入 @CacheConfig，我们则需要使用 value 来指定缓存名称
这里如果需要多个 key 时，需要使用 “:” 来连接，如：
```java
@Cacheable(value = "user", key = "#name+':'+#phone")
```

3、`@CachePut`

这个注解一般加在更新方法上，当数据库中的数据更新后，缓存中的数据也要跟着更新，使用该注解，可以将方法的返回值自动更新到已经存在的 key 上，示例代码如下：

```java
@Override
@CachePut(value = "user", key = "#id")
public User updateUserById(User user) {
 return userMapper.updateUserById(user);
}
```

4、`@CacheEvict`

这个注解一般加在删除方法上，当数据库中的数据删除后，相关的缓存数据也要自动清除，该注解在使用的时候也可以配置按照某种条件删除（ condition 属性）或者或者配置清除所有缓存（ allEntries 属性），示例代码如下：

```java
@Override
@CacheEvict(value = "user", key = "#id")
public void deleteUserById(Long id) {
 userMapper.deleteUserById(id);
}

```


## 四、总结

SpringBoot 中使用 Ehcache 比较简单，只需要简单配置，说白了还是 Spring Cache 的用法，合理使用缓存机制，可以很好地提高项目的响应速度。

到此这篇关于SpringBoot中使用Ehcache的文章就介绍到这了,更多相关SpringBoot使用Ehcache内容请搜索脚本之家以前的文章或继续浏览下面的相关文章希望大家以后多多支持脚本之家！
