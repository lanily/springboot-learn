# web-msg

#### 介绍
基于WebSocket的网页消息交互服务，主要用于前端消息通知（推送），前后端实时交互。   
增加WebSocket接口转发机制，并兼容Spring Mvc接口。   
支持单例、分布式部署方式。   
目前通过Redis共享消息数据，可靠性有待验证。


#### 软件架构
SpringBoot2.1 + WebSocket2.1 + Hutool4.6   
![avatar](https://gitee.com/wangbaishi_libi/web-msg/raw/master/structure.png)

#### 安装教程

以SpringBoot方式启动

#### 使用说明

1.  配置   
message.serverRoot websocket请求根路径   
message.sessionMode 支持三种single、multi、distribute，具体可查阅三种对应SessionManager   
message.msgPoolKey 通过redis共享传递的消息池
2.  核心模块   
MessageHandler  统一接收消息   
MessagePoster   统一发送消息   
SessionManager  管理WebsocketSession
MessageDispatcher   将接收的消息按请求路径分发到对应的WSController方法   
DistributeMessageTask 分布式消息定时任务，寻找分布式消息对应的本地session实例，并发送   
RedisMessageTask 基于Redis消息定时任务，寻找共享消息对应的本地session实例，并发送   

3.  部署   
若单独（分布式）服务部署，可通过redis共享消息；   
若集成到应用服务，可调用MessagePoster发送消息。

