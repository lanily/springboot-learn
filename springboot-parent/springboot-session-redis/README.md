# SpringBoot 集成spring session redis实现session共享



## 使用 Redis 实现 Session 共享

## 1 什么是 Session

由于 HTTP 协议是无状态的协议，因而服务端需要记录用户的状态时，就需要用某种机制来识具体的用户。Session 是另一种记录客户状态的机制，不同的是 Cookie 保存在客户端浏览器中，而 Session 保存在服务器上。客户端浏览器访问服务器的时候，服务器把客户端信息以某种形式记录在服务器上，这就是 Session。客户端浏览器再次访问时只需要从该 Session 中查找该客户的状态就可以了。

## 2 为什么需要同步session ？

> 当用户量比较大时候一个tomcat可能无法处理更多的请求，超过单个tomcat的承受能力，可能会出现用户等待，严重的导致tomcat宕机。

[![说明](https://www.cicoding.cn/images/springboot/892644927-5d1c4942a2cb6_articlex.png)](https://www.cicoding.cn/images/springboot/892644927-5d1c4942a2cb6_articlex.png)

> 这时候我们后端可能会采用多个tomcat去处理请求，分派请求，不同请求让多个tomcat分担处理。
>
> 登录的时候可能采用的是tomca1，下单的时候可能采用的是tomcat2 等等等。

若没有session共享同步，可能在tomcat1登录了，下一次请求被分派到tomcat2上，这时候用户就需要重新登录。

> 在实际工作中我们建议使用外部的缓存设备来共享 Session，避免单个节点挂掉而影响服务，使用外部缓存 Session 后，我们的
> 共享数据都会放到外部缓存容器中，服务本身就会变成无状态的服务，可以随意的根据流量的大小增加或者减少负载的设备。

目前主流的分布式 Session 管理有两种方案。

### 1 Session 复制

部分 Web 服务器能够支持 Session 复制功能，如 Tomcat。用户可以通过修改 Web 服务器的配置文件，让 Web 服务器进行 Session 复制，保持每一个服务器节点的 Session 数据都能达到一致。

这种方案的实现依赖于 Web 服务器，需要 Web 服务器有 Session 复制功能。当 Web 应用中 Session 数量较多的时候，每个服务器节点都需要有一部分内存用来存放 Session，将会占用大量内存资源。同时大量的 Session 对象通过网络传输进行复制，不但占用了网络资源，还会因为复制同步出现延迟，导致程序运行错误。

在微服务架构中，往往需要 N 个服务端来共同支持服务，不建议采用这种方案。

这种session 管理方式，可以参考此篇文章：[传送门](https://blog.csdn.net/weixin_38361347/article/details/82629025)

### 2 Session 集中存储

在单独的服务器或服务器集群上使用缓存技术，如 Redis 存储 Session 数据，集中管理所有的 Session，所有的 Web 服务器都从这个存储介质中存取对应的 Session，实现 Session 共享。将 Session 信息从应用中剥离出来后，其实就达到了服务的无状态化，这样就方便在业务极速发展时水平扩充。

**Spring Session**
Spring Session 提供了一套创建和管理 Servlet HttpSession 的方案。Spring Session 提供了集群 Session（Clustered Sessions）功能，默认采用外置的 Redis 来存储 Session 数据，以此来解决 Session 共享的问题。

Spring Session 为企业级 Java 应用的 Session 管理带来了革新，使得以下的功能更加容易实现：

> API 和用于管理用户会话的实现； HttpSession，允许以应用程序容器（即 Tomcat）中性的方式替换 HttpSession；
> 将 Session 所保存的状态卸载到特定的外部 Session 存储中，如 Redis 或 Apache Geode
> 中，它们能够以独立于应用服务器的方式提供高质量的集群； 支持每个浏览器上使用多个
> Session，从而能够很容易地构建更加丰富的终端用户体验； 控制 Session ID
> 如何在客户端和服务器之间进行交换，这样的话就能很容易地编写 Restful API，因为它可以从 HTTP 头信息中获取 Session
> ID，而不必再依赖于 cookie； 当用户使用 WebSocket 发送请求的时候，能够保持 HttpSession 处于活跃状态。
> 需要说明的很重要的一点就是，Spring Session 的核心项目并不依赖于 Spring 框架，因此，我们甚至能够将其应用于不使用
> Spring 框架的项目中。

## 3 项目准备

### 3.1 pom文件添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

### 3.2 配置文件添加redis 链接

```plain
spring.redis.database=0
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=
spring.redis.jedis.pool.max-idle=8
spring.redis.jedis.pool.max-wait=-1
spring.redis.jedis.pool.min-idle=0
spring.redis.timeout=5000

server.port=8081
```

### 3.3添加redis session配置

```java
package com.hsiao.springboot.session.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration  
@EnableRedisHttpSession
public class RedisSessionConfig {  
}
```

### 3.4 获取sessionid Controller

```java
package com.hsiao.springboot.session.redis.controller;
 
import java.util.HashMap;
import java.util.Map;
 
import javax.servlet.http.HttpSession;
 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class SessionController {
 
    @RequestMapping(value = "/setsession")
    public Object setSession(@RequestParam(required=false) String value, HttpSession session) {
        session.setAttribute("value", value);
        return session.getId();
    }
    
    @RequestMapping(value = "/getsession")
    public Object getSession(HttpSession session) {
        Object value = session.getAttribute("value");
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", session.getId());
        map.put("value", value);
        return map;
    }
 
}
```

3.5 
考虑到一会 Spring Boot 将以集群的方式启动 ，为了获取每一个请求到底是哪一个 Spring Boot 提供的服务，需要在每次请求时返回当前服务的端口号，因此这里我注入了 server.port 。

接下来 ，项目打包：



打包之后，启动项目的两个实例：

java -jar sessionshare-0.0.1-SNAPSHOT.jar --server.port=8080
java -jar sessionshare-0.0.1-SNAPSHOT.jar --server.port=8081
然后先访问 localhost:8080/set 向 8080 这个服务的 Session 中保存一个变量，访问完成后，数据就已经自动同步到 Redis 中 了 ：



然后，再调用 localhost:8081/get 接口，就可以获取到 8080 服务的 session 中的数据：



此时关于 session 共享的配置就已经全部完成了，session 共享的效果我们已经看到了，但是每次访问都是我自己手动切换服务实例，因此，接下来我们来引入 Nginx ，实现服务实例自动切换。

1.4 引入 Nginx
很简单，进入 Nginx 的安装目录的 conf 目录下（默认是在 /usr/local/nginx/conf），编辑 nginx.conf 文件:



在这段配置中：

upstream 表示配置上游服务器
javaboy.org 表示服务器集群的名字，这个可以随意取名字
upstream 里边配置的是一个个的单独服务
weight 表示服务的权重，意味者将有多少比例的请求从 Nginx 上转发到该服务上
location 中的 proxy_pass 表示请求转发的地址，/ 表示拦截到所有的请求，转发转发到刚刚配置好的服务集群中
proxy_redirect 表示设置当发生重定向请求时，nginx 自动修正响应头数据（默认是 Tomcat 返回重定向，此时重定向的地址是 Tomcat 的地址，我们需要将之修改使之成为 Nginx 的地址）。
配置完成后，将本地的 Spring Boot 打包好的 jar 上传到 Linux ，然后在 Linux 上分别启动两个 Spring Boot 实例：

nohup java -jar sessionshare-0.0.1-SNAPSHOT.jar --server.port=8080 &
nohup java -jar sessionshare-0.0.1-SNAPSHOT.jar --server.port=8081 &
其中

nohup 表示当终端关闭时，Spring Boot 不要停止运行
& 表示让 Spring Boot 在后台启动
配置完成后，重启 Nginx：

/usr/local/nginx/sbin/nginx -s reload
Nginx 启动成功后，我们首先手动清除 Redis 上的数据，然后访问 192.168.66.128/set 表示向 session 中保存数据，这个请求首先会到达 Nginx 上，再由 Nginx 转发给某一个 Spring Boot 实例：

### 3.5 测试

访问http://localhost:8080/setsession

[![image.png](https://www.cicoding.cn/images/springboot/1570759299435-8b02890d-aac8-4e4a-ad9f-f5d80b877285.png)](https://www.cicoding.cn/images/springboot/1570759299435-8b02890d-aac8-4e4a-ad9f-f5d80b877285.png)

启动8081 http://localhost:8081/getsession

[![image.png](https://www.cicoding.cn/images/springboot/1570759346002-4f80663c-e337-4d19-b8a3-1a6ec17171db.png)](https://www.cicoding.cn/images/springboot/1570759346002-4f80663c-e337-4d19-b8a3-1a6ec17171db.png)

### 参考文献：

[springboot2.1入门系列四 Spring Session实现session共享](https://blog.csdn.net/bowei026/article/details/86531614)
[Redis共享Session原理及示例](https://blog.csdn.net/moxiaomomo/article/details/82749865)
[SpringBoot 使用 Redis 实现 Session 共享](https://segmentfault.com/a/1190000019625173)
