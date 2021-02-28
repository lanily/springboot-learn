# 说明

## logback企业实践：`springboot-logback-demo`

基于springboot+logback，对日志输出框架的使用示例，结合在企业中的实践进行详细说明。

1. `logback-simple-demo`

- 示例功能： 使用springboot+logback构建示例工程及配置描述，对logback配置文件的详述及使用，实现按日志级别输出到文件功能。
- 文章：[springboot+logback日志输出企业实践（上）](https://mianshenglee.github.io/2019/11/28/logback1.html)

2. `logback-advance-demo`

- 示例功能：对 logback 的进阶使用进行描述，主要包括日志异步输出，多环境日志配置以及使用MDC进行分布式系统请求追踪。
- 文章：[springboot+logback日志输出企业实践（下）]( https://mianshenglee.github.io/2019/11/29/logback2.html )



Spring Boot中的日志收集，日志是追踪错误定位问题的关键，特别在生产环境中，我们需要通过日志快速定位解决问题。

Springboot的日志的框架比较丰富，而且Springboot本身就内置了日志功能，不过实际项目中会出现：只记录想要的日志，日志输出到磁盘，按天归档，日志信息同步到其他系统等功能。这些是Springboot本身就内置了日志功能不具备的。所以我推荐使用logback。下面我们就以logback讲讲Spring Boot中的日志收集。

 

为什么要统一日志
前面我们说了Springboot 本身就可以日志功能，为什么还需要统一规范日志？

1、日志统一，方便查阅管理。

2、日志分割归档功能。

3、日志持久化功能。

4、方便日志系统（ELK）收集。


https://cloud.tencent.com/developer/article/1646435
