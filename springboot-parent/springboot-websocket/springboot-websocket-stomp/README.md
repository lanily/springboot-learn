# springboot stomp实现websocket

## 一、WebSocket 简介

![img](../docs/images/springboot-websocket-1002.png)

  WebSocket 是一种基于 TCP 的网络协议。在 2009 年诞生，于 2011 年被 IETF 定为标准 RFC 6455 通信标准，并由 RFC7936 补充规范。WebSocket API 也被 W3C 定为标准。

WebSocket 也是一种全双工通信的协议，既允许客户端向服务器主动发送消息，也允许服务器主动向客户端发送消息。在 WebSocket 中，浏览器和服务器只需要完成一次握手，两者之间就可以建立持久性的连接，进行双向数据传输。

## 二、WebSocket 特点

- 连接握手阶段使用 HTTP 协议；
- 协议标识符是 ws，如果采用加密则是 wss；
- 数据格式比较轻量，性能开销小，通信高效；
- 没有同源限制，客户端可以与任意服务器通信；
- 建立在 TCP 协议之上，服务器端的实现比较容易；
- 通过 WebSocket 可以发送文本，也可以发送二进制数据；
- 与 HTTP 协议有着良好的兼容性。默认端口也是 80 和 443，并且握手阶段采用 HTTP 协议，因此握手时不容易屏蔽，能通过各种 HTTP 代理服务器；

## 三、为什么需要 WebSocket

  谈起为什么需要 WebSocket 前，那得先了解在没有 WebSocket 那段时间说起，那时候基于 Web 的消息基本上是靠 Http 协议进行通信，而经常有”聊天室”、”消息推送”、”股票信 息实时动态”等这样需求，而实现这样的需求常用的有以下几种解决方案：

![img](../docs/images/springboot-websocket-1003.png)


**(1)、短轮询(Traditional Polling)**

短轮询是指客户端每隔一段时间就询问一次服务器是否有新的消息，如果有就接收消息。这样方式会增加很多次无意义的发送请求信息，每次都会耗费流量及处理器资源。

- 优点：短连接，服务器处理简单，支持跨域、浏览器兼容性较好。
- 缺点：有一定延迟、服务器压力较大，浪费带宽流量、大部分是无效请求。

---

**(2)、长轮询(Long Polling)**

长轮询是段轮询的改进，客户端执行 HTTP 请求发送消息到服务器后，等待服务器回应，如果没有新的消息就一直等待，知道服务器有新消息传回或者超时。这也是个反复的过程，这种做法只是减小了网络带宽和处理器的消耗，但是带来的问题是导致消息实时性低，延迟严重。而且也是基于循环，最根本的带宽及处理器资源占用并没有得到有效的解决。

- 优点：减少轮询次数，低延迟，浏览器兼容性较好。
- 缺点：服务器需要保持大量连接。

---

**(3)、服务器发送事件(Server-Sent Event)**

> 目前除了 IE/Edge，其他浏览器都支持。

服务器发送事件是一种服务器向浏览器客户端发起数据传输的技术。一旦创建了初始连接，事件流将保持打开状态，直到客户端关闭。该技术通过传统的 HTTP 发送，并具有 WebSockets 缺乏的各种功能，例如”自动重新连接”、”事件ID” 及 “发送任意事件”的能力。

服务器发送事件是单向通道，只能服务器向浏览器发送，因为流信息本质上就是下载。

- 优点：适用于更新频繁、低延迟并且数据都是从服务端发到客户端。
- 缺点：浏览器兼容难度高。

---

显然，上面这几种方式都有各自的优缺点，虽然靠轮询方式能够实现这些一些功能，但是其对性能的开销和低效率是非常致命的，尤其是在移动端流行的现在。现在客户端与服务端双向通信的需求越来越多，且现在的浏览器大部分都支持 WebSocket。所以对实时性和双向通信及其效率有要求的话，比较推荐使用 WebSocket。

## 四、WebSocket 连接流程

**(1)、客户端先用带有 Upgrade:Websocket 请求头的 HTTP 请求，向服务器端发起连接请求，实现握手(HandShake)。**

客户端 HTTP 请求的 Header 头信息如下：

bash

```bash
Connection: Upgrade
Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits
Sec-WebSocket-Key: IRQYhWINfX5Fh1zdocDl6Q==
Sec-WebSocket-Version: 13
Upgrade: websocket
```

- **Connection：** Upgrade 表示要升级协议。
- **Upgrade：** Websocket 要升级协议到 websocket 协议。
- **Sec-WebSocket-Extensions：** 表示客户端所希望执行的扩展(如消息压缩插件)。
- **Sec-WebSocket-Key：** 主要用于WebSocket协议的校验，对应服务端响应头的 Sec-WebSocket-Accept。
- **Sec-WebSocket-Version：** 表示websocket的版本。如果服务端不支持该版本，需要返回一个Sec-WebSocket-Versionheader，里面包含服务端支持的版本号。

**(2)、握手成功后，由 HTTP 协议升级成 Websocket 协议，进行长连接通信，两端相互传递信息。**

服务端响应的 HTTP Header 头信息如下：

bash

```bash
Connection: upgrade
Sec-Websocket-Accept: TSF8/KitM+yYRbXmjclgl7DwbHk=
Upgrade: websocket
```

- **Connection：** Upgrade 表示要升级协议。
- **Upgrade：** Websocket 要升级协议到 websocket 协议。
- **Sec-Websocket-Accept：** 对应 Sec-WebSocket-Key 生成的值，主要是返回给客户端，让客户端对此值进行校验，证明服务端支持 WebSocket。

## 五、WebSocket 使用场景

- **数据流状态：** 比如说上传下载文件，文件进度，文件是否上传成功。
- **协同编辑文档：** 同一份文档，编辑状态得同步到所有参与的用户界面上。
- **多玩家游戏：** 很多游戏都是协同作战的，玩家的操作和状态肯定需要及时同步到所有玩家。
- **多人聊天：** 很多场景下都需要多人参与讨论聊天，用户发送的消息得第一时间同步到所有用户。
- **社交订阅：** 有时候我们需要及时收到订阅消息，比如说开奖通知，比如说在线邀请，支付结果等。
- **股票虚拟货币价格：** 股票和虚拟货币的价格都是实时波动的，价格跟用户的操作息息相关，及时推送对用户跟盘有很大的帮助。

## 六、WebSocket 中子协议支持

  WebSocket 确实指定了一种消息传递体系结构，但并不强制使用任何特定的消息传递协议。而且它是 TCP 上的一个非常薄的层，它将字节流转换为消息流（文本或二进制）仅此而已。由应用程序来解释消息的含义。

与 HTTP（它是应用程序级协议）不同，在 WebSocket 协议中，**传入消息中根本没有足够的信息供框架或容器知道如何路由或处理它**。因此，对于非常琐碎的应用程序而言 WebSocket 协议的级别可以说太低了。**可以做到的是引导在其上面再创建一层框架。这就相当于当今大多数 Web 应用程序使用的是 Web 框架，而不直接仅使用 Servlet API 进行编码一样**。

WebSocket RFC 定义了子协议的使用。在握手过程中，客户机和服务器可以使用头 Sec-WebSocket 协议商定子协议，即使不需要使用子协议，而是用更高的应用程序级协议，但应用程序仍需要选择客户端和服务器都可以理解的消息格式。且该格式可以是自定义的、特定于框架的或标准的消息传递协议。

Spring 框架支持使用 STOMP，这是一个简单的消息传递协议，最初创建用于脚本语言，框架灵感来自 HTTP。STOMP 被广泛支持，非常适合在 WebSocket 和 web 上使用。

## 七、什么是 STOMP 协议

> 关于详细的 STOMP 请查看[STOMP 协议规范](https://stomp.github.io/stomp-specification-1.2.html)了解。

**(1)、STOMP 协议概述**

  STOMP（Simple Text-Orientated Messaging Protocol）是一种简单的面向文本的消息传递协议。它提供了一个可互操作的连接格式，允许 STOMP 客户端与任意 STOMP 消息代理（Broker）进行交互。STOMP 协议由于设计简单，易于开发客户端，因此在多种语言和多种平台上得到广泛地应用。

**(2)、简单介绍可以分为以下几点：**

- STOMP 是基于帧的协议，其帧以 HTTP 为模型。
- STOMP 框架由命令，一组可选的标头和可选的主体组成。
- STOMP 基于文本，但也允许传输二进制消息。
- STOMP 的默认编码为 UTF-8，但它支持消息正文的替代编码的规范。

**(3)、STOMP 客户端是一种用户代理，可以分为两种模式运行：**

- 作为生产者，通过 SEND 帧将消息发送到目标服务器上。
- 作为消费者，对目标地址发送 SUBSCRIBE 帧，并作为 MESSAGE 帧从服务器接收消息。

**(4)、STOMP 帧**

STOMP 是基于帧的协议，其帧以 HTTP 为模型。STOMP 结构为：

bash

```bash
COMMAND
header1:value1
header2:value2
Body^@
```

客户端可以使用 SEND 或 SUBSCRIBE 命令发送或订阅消息，还可以使用 “destination” 头来描述消息的内容和接收者。这支持一种简单的发布-订阅机制，可用于通过代理将消息发送到其他连接的客户端，或将消息发送到服务器以请求执行某些工作。

**(5)、Stomp 常用帧**

STOMP 的客户端和服务器之间的通信是通过”**帧**“（Frame）实现的，每个帧由多”**行**“（Line）组成，其包含的帧如下：

- *Connecting Frames：*
    - [CONNECT（连接）](https://stomp.github.io/stomp-specification-1.2.html#CONNECT_or_STOMP_Frame)
    - [CONNECTED（成功连接）](https://stomp.github.io/stomp-specification-1.2.html#CONNECTED_Frame)
- *Client Frames：*
    - [SEND（发送）](https://stomp.github.io/stomp-specification-1.2.html#SEND)
    - [SUBSRIBE（订阅）](https://stomp.github.io/stomp-specification-1.2.html#SUBSCRIBE)
    - [UNSUBSCRIBE（取消订阅）](https://stomp.github.io/stomp-specification-1.2.html#UNSUBSCRIBE)
    - [BEGIN（开始）](https://stomp.github.io/stomp-specification-1.2.html#BEGIN)
    - [COMMIT（提交）](https://stomp.github.io/stomp-specification-1.2.html#COMMIT)
    - [ABORT（中断）](https://stomp.github.io/stomp-specification-1.2.html#ABORT)
    - [ACK（确认））](https://stomp.github.io/stomp-specification-1.2.html#ACK)
    - [NACK（否认））](https://stomp.github.io/stomp-specification-1.2.html#NACK)
    - [DISCONNECT（断开连接））](https://stomp.github.io/stomp-specification-1.2.html#DISCONNECT)
- *Server Frames：*
    - [MESSAGE（消息））](https://stomp.github.io/stomp-specification-1.2.html#MESSAGE)
    - [RECEIPT（接收））](https://stomp.github.io/stomp-specification-1.2.html#RECEIPT)
    - [ERROR（错误））](https://stomp.github.io/stomp-specification-1.2.html#ERROR)

**(6)、Stomp 与 WebSocket 的关系**

  直接使用 WebSocket 就很类似于使用 TCP 套接字来编写 Web 应用，因为没有高层级的应用协议（wire protocol），因而就需要我们定义应用之间所发送消息的语义，还需要确保连接的两端都能遵循这些语义。同 HTTP 在 TCP 套接字上添加请求-响应模型层一样，STOMP 在 WebSocket 之上提供了一个基于帧的线路格式层，用来定义消息语义。

**(7)、使用 STOMP 作为 WebSocket 子协议的好处**

- 无需发明自定义消息格式
- 在浏览器中 使用现有的stomp.js客户端
- 能够根据目的地将消息路由到
- 可以使用成熟的消息代理（例如RabbitMQ，ActiveMQ等）进行广播的选项
- 使用STOMP（相对于普通 WebSocket）使 Spring Framework 能够为应用程序级使用提供编程模型，就像 Spring MVC 提供基于 HTTP 的编程模型一样。

## 八、Spring 封装的 STOMP

 使用 Spring 的 STOMP 支持时，Spring WebSocket 应用程序充当客户端的 STOMP 代理。消息被路由到 @Controller 消息处理方法或简单的内存中代理，该代理跟踪订阅并向订阅的用户广播消息。还可以将 Spring 配置为与专用的 STOMP 代理（例如 RabbitMQ，ActiveM Q等）一起使用，以实际广播消息。在那种情况下，Spring 维护与代理的 TCP 连接，将消息中继到该代理，并将消息从该代理向下传递到已连接的 WebSocket 客户端。因此 Spring Web 应用程序可以依赖基于统一 HTTP 的安全性，通用验证以及熟悉的编程模型消息处理工作。

Spring 官方提供的处理流图:

![img](../docs/images/progress-bar-stripe-loader.png)

**img**

上面中的一些概念关键词：

- **Message：**消息，里面带有 header 和 payload。

- **MessageHandler：**处理 client 消息的实体。

- MessageChannel：

  解耦消息发送者与消息接收者的实体

    - clientInboundChannel：用于从 WebSocket 客户端接收消息。
    - clientOutboundChannel：用于将服务器消息发送给 WebSocket 客户端。
    - brokerChannel：用于从服务器端、应用程序中向消息代理发送消息

- **Broker：** 存放消息的中间件，client 可以订阅 broker 中的消息。

上面的设置包括3个消息通道：

- **clientInboundChannel：** 用于来自WebSocket客户端的消息。
- **clientOutboundChannel：** 用于向WebSocket客户端发送消息。
- **brokerChannel：** 从应用程序内部发送给代理的消息。

## 九、实现 WebSocket

**示例地址：**

- [WebSocket 示例一：实现简单的广播模式](https://github.com/my-dlq/blog-example/tree/master/springboot/springboot-websocket-example/springboot-websocket-topic-example/)
- [WebSocket 示例二：实现点对点模式(引入 Spring Security 实现鉴权)](https://github.com/my-dlq/blog-example/tree/master/springboot/springboot-websocket-example/springboot-websocket-queue-security-example/)
- [WebSocket 示例三：实现点对点模式(根据请求头 Header 实现鉴权)](https://github.com/my-dlq/blog-example/tree/master/springboot/springboot-websocket-example/springboot-websocket-queue-header-example/)
- [WebSocket 示例四：实现点对点模式(根据 HTTP Session 实现鉴权)](https://github.com/my-dlq/blog-example/tree/master/springboot/springboot-websocket-example/springboot-websocket-queue-session-example/)

**参考地址：**

- [Stomp 1.2 协议规范](https://stomp.github.io/stomp-specification-1.2.html)
- [Spring 框架对 WebSocket 的支持](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html#websocket-intro-sub-protocol)

**环境信息：**

- SpringBoot 版本：2.3.1.RELEASE
- Spring WebSocket 版本：5.2.7.RELEASE
- 前端使用的框架：bootstrap 3.4.1、Jquery 3.5.1、sockjs-client 1.4.0、stomp.js 2.3.3
  详情请查看博客，地址为： http://www.mydlq.club/article/86/
