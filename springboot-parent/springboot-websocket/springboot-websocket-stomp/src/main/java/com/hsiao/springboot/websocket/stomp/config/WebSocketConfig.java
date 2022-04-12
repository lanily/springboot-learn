package com.hsiao.springboot.websocket.stomp.config;


import com.hsiao.springboot.websocket.stomp.auth.AuthChannelInterceptor;
import com.hsiao.springboot.websocket.stomp.auth.HttpWebSocketHandlerDecoratorFactory;
import com.hsiao.springboot.websocket.stomp.constants.WsConstants;
import com.hsiao.springboot.websocket.stomp.constants.WsConstants.Broker;
import com.hsiao.springboot.websocket.stomp.model.WebSocketUser;
import com.hsiao.springboot.websocket.stomp.util.JwtUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 *
 * WeSocket配置类
 *
 * @description: 开启使用STOMP协议来传输基于代理（message broker）的消息
 * 启用后控制器支持@MessageMapping注解
 * EnableWebSocketMessageBroker -- 注解开放STOMP协议来传输基于代理的消息，此时控制器支持@MessageMapping
 * 核心内容讲解：
 * 1）、@EnableWebSocketMessageBroker：用于开启stomp协议，这样就能支持@MessageMapping注解，类似于@requestMapping一样，同时前端可以使用Stomp客户端进行通讯；
 * 2）、registerStompEndpoints实现：主要用来注册端点地址、开启跨域授权、增加拦截器、声明SockJS，这也是前端选择SockJS的原因，因为spring项目本身就支持；
 * 3）、configureMessageBroker实现：主要用来设置客户端订阅消息的路径(可以多个)、点对点订阅路径前缀的设置、访问服务端@MessageMapping接口的前缀路径、心跳设置等；
 *
 *
 * @projectName springboot-parent
 * @title: WebSocketConfig
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private AuthChannelInterceptor authChannelInterceptor;

    /**
     * 注册stomp端点
     * 注册STOMP协议节点并映射url
     *
     * 添加这个Endpoint，这样在网页中就可以通过websocket连接上服务,
     * 也就是我们配置websocket的服务地址,并且可以指定是否使用socketjs
     * 1. 将 /ws 路径注册为STOMP的端点，
     *    用户连接了这个端点后就可以进行websocket通讯，支持socketJs
     * 2. setAllowedOrigins("*")表示可以跨域
     * 3. withSockJS()表示支持socktJS访问
     * 4. addInterceptors 添加自定义拦截器，这个拦截器是上一个demo自己定义的获取httpsession的拦截器
     * 5. addInterceptors 添加拦截处理，这里MyPrincipalHandshakeHandler 封装的认证用户信息
     *
     * @param registry stomp 端点注册对象
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 允许原生的websocket
//        registry.addEndpoint("/ws") // 请求地址：ws://ip:port/ws
//                .setAllowedOrigins("*");

        registry.addEndpoint(WsConstants.WS_PATH);
        // 允许sockJS
        //
        // 注册一个 Stomp 的节点(endpoint)，并指定使用 SockJS 协议。请求地址：http://ip:port/ws
        registry.addEndpoint(WsConstants.WS_PATH)
                //添加websocket握手拦截器，拦截器方式1，暂不用
                .addInterceptors(getHandshakeInterceptor())
                //添加websocket握手处理器
//                .setHandshakeHandler(getDefaultHandshakeHandler())
                //设置允许可跨域的域名
//                .setAllowedOrigins("hk.hsbc")
                .setAllowedOrigins("*")
                //指定使用Sockjs函数
                .withSockJS();
    }

    /**
     * websocket 握手拦截器
     * 可做一些用户认证拦截处理
     * @return
     */
    private HandshakeInterceptor getHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            /**
             * websocket 握手连接
             *
             * 实现HandshakeInterceptor接口在beforeHandshake方法中来处理，这种方式缺点是无法获取header中的值，只能获取url中的参数，如果token用jwt等很长的，用这种方式实现并不友好。
             * @param request
             * @param response
             * @param wsHandler
             * @param attributes
             * @return 是否同意握手
             * @throws Exception
             */
            @Override
            public boolean beforeHandshake(ServerHttpRequest request,
                    ServerHttpResponse response, WebSocketHandler wsHandler,
                    Map<String, Object> attributes) throws Exception {
                ServletServerHttpRequest req = (ServletServerHttpRequest) request;
/*                if (request instanceof ServletServerHttpRequest) {
                    HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                    // 获取请求链接以前的token参数.
                    Enumeration enu = servletRequest.getParameterNames();
                    while (enu.hasMoreElements()) {
                        String paraName = (String) enu.nextElement();
                        System.out.println(paraName + ": " + servletRequest.getParameter(paraName));
                    }
                    // 从session中获取到当前登陆的用户信息. 做为socket的帐号信息. session的的WEBSOCKET_USERNAME信息,在用户打开页面的时候设置.
                    String userName = (String) servletRequest.getSession().getAttribute("WEBSOCKET_USERNAME");
                    attributes.put("WEBSOCKET_USERNAME", userName);
                }*/

                // 通过url的query参数获取认证参数
                String token = req.getServletRequest().getParameter("token");
                WebSocketUser user = JwtUtils.parseToken(token);
                if (Objects.nonNull(user)) {
                    // 如果存在用户信息，将用户名赋值，后期发送时，可以指定用户名即可发送到对应用户
                    Principal principal = () -> user.getName();
                    attributes.put("user", principal);
                    log.info("authentication successfully");
                    // 保证认证用户
                    return true;
                }
                // 根据token认证用户，不通过返回拒绝握手
                return false;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                    WebSocketHandler handler, Exception e) {
                if (request instanceof ServletServerHttpRequest) {
                    HttpServletRequest req = ((ServletServerHttpRequest) request)
                            .getServletRequest();
                    HttpServletResponse resp = ((ServletServerHttpResponse) response)
                            .getServletResponse();
                    if (!StringUtils.isEmpty(req.getHeader("sec-websocket-protocol"))) {
                        try {
                            System.out.println(URLDecoder
                                    .decode(req.getHeader("sec-websocket-protocol"), "utf-8"));
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                        resp.addHeader("sec-websocket-protocol",
                                req.getHeader("sec-websocket-protocol"));
                    }
                }
            }
        };
    }

    /**
     * websocket 握手处理器
     * 用于与正在建立会话过程中的websocket的用户相关联的方法，可以在此处配置进行关联的用户信息
     * @return
     */
    private HandshakeHandler getDefaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            /**
             * 用于与正在建立会话过程中的 WebSocket 的用户相关联的方法，可以在此处配置进行关联的用户信息。
             * @param request 握手请求对象
             * @param wsHandler WebSocket 处理器
             * @param attributes 从 HTTP 握手到与 WebSocket 会话关联的属性
             * @return 对于 WebSocket 的会话的用户或者 null
             */
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                    Map<String, Object> attributes) {
                Principal user = (Principal) attributes.get("user");
                return user::getName;
            }
        };
    }


    /**
     * 拦截器方式2
     * 配置通道拦截器，用于获取Header的token进行鉴权
     * 实现ChannelInterceptor接口在preSend方法中来处理，这种方式可以获取header中的值，而且还可以设置用户信息等
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(authChannelInterceptor);
    }

    /**
     * 定义一些消息连接规范（也可不设置）
     * 配置消息代理
     *
     *
     * 方法configureMessageBroker做两件事：
     *
     * 创建具有一个或多个用于发送和接收消息的目的地的内存中消息代理。在上面的示例中, 定义了两个目标前缀：topic和queue。它们遵循的约定是, 要通过pub-sub模型传递给所有订阅的客户端的消息的目的地应以topic为前缀。另一方面, 私人消息的目的地通常以队列为前缀。
     * 定义前缀应用程序, 该应用程序用于过滤由@MessageMapping注释的方法处理的目标, 你将在控制器中实现该方法。控制器在处理了消息之后, 会将其发送给代理。
     * @param registry 消息代理注册对象
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 设置客户端接收消息地址的前缀（可不设置）
        // topic用来广播，user用来实现点对点
        registry.enableSimpleBroker(
                // 广播消息前缀
                Broker.BROKER_TOPIC,
                // 点对点消息前缀
                Broker.BROKER_QUEUE,
                // since https://localcoder.org/spring-websocket-convertandsendtouser-not-working-but-convertandsend-working
                WsConstants.WS_USER_PREFIX);
        // 设置心跳调度器，指定使用上面定义的调度器
//                .setTaskScheduler(webSocketHeartbeatTaskScheduler())
        // 心跳频率 {服务端发送频率， 客户端发送频率}ms, 5秒一次
//                .setHeartbeatValue(new long[5000, 5000])
        /*
         * spring 内置broker对象
         * 1. 配置代理域，可以配置多个，此段代码配置代理目的地的前缀为 /topic 或者 /queue
         *    我们就可以在配置的域上向客户端推送消息
         * 2，进行心跳设置，第一值表示server最小能保证发的心跳间隔毫秒数, 第二个值代码server希望client发的心跳间隔毫秒数
         * 3. 可以配置心跳线程调度器 setHeartbeatValue这个不能单独设置，不然不起作用，要配合setTaskScheduler才可以生效
         *    调度器我们可以自己写一个，也可以自己使用默认的调度器 new DefaultManagedTaskScheduler()
         */
//        registry.enableSimpleBroker("/topic","/queue")
//                .setHeartbeatValue(new long[]{10000,10000})
//                .setTaskScheduler(taskScheduler);


        /*
         *  enableStompBrokerRelay 配置外部的STOMP服务，需要安装额外的支持 比如rabbitmq或activemq
         * 1. 配置代理域，可以配置多个，此段代码配置代理目的地的前缀为 /topic 或者 /queue
         *    我们就可以在配置的域上向客户端推送消息
         * 3. 可以通过 setRelayHost 配置代理监听的host,默认为localhost
         * 4. 可以通过 setRelayPort 配置代理监听的端口，默认为61613
         * 5. 可以通过 setClientLogin 和 setClientPasscode 配置账号和密码
         * 6. setxxx这种设置方法是可选的，根据业务需要自行配置，也可以使用默认配置
         */
//        registry.enableStompBrokerRelay("/topic","/queue")
//        .setRelayHost("rabbit.someotherserver")
//        .setRelayPort(62623);
//        .setClientLogin("userName")
//        .setClientPasscode("password");

        /*
         * 设置客户端向服务器发送消息的地址前缀（可不设置，这里设置为/app，默认是空字符串）
         * 加了之后前端访问接口就是stompClient.send("/app/send")。
         *  "/app" 为配置应用服务器的地址前缀，表示所有以/app 开头的客户端消息或请求
         *  都会路由到带有@MessageMapping 注解的方法中
         */
        registry.setApplicationDestinationPrefixes(WsConstants.WS_APP_PREFIX);

        /*
         * 设置客户端接收点对点消息地址的前缀，默认为/user，加了后默认值就会被覆盖。
         *  1. 配置一对一消息前缀， 客户端接收一对一消息需要配置的前缀 如“'/user/'+userid + '/message'”，
         *     是客户端订阅一对一消息的地址 stompClient.subscribe js方法调用的地址
         *  2. 使用@SendToUser发送私信的规则不是这个参数设定，在框架内部是用UserDestinationMessageHandler处理，
         *     而不是而不是 AnnotationMethodMessageHandler 或  SimpleBrokerMessageHandler
         *     or StompBrokerRelayMessageHandler，是在@SendToUser的URL前加“user+sessionId"组成
         */
        registry.setUserDestinationPrefix(WsConstants.WS_USER_PREFIX);
        registry.setPreservePublishOrder(true);
    }

    @Bean
    public ThreadPoolTaskScheduler webSocketHeartbeatTaskScheduler() {
        // 自定义调度器，用于控制心跳线程
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 线程池线程数，心跳连接开线程
        taskScheduler.setPoolSize(1);
        // 线程名前缀
        taskScheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        // 初始化
        taskScheduler.initialize();
        return taskScheduler;
    }


    /**
     * 配置发送与接收的消息参数，可以指定消息字节大小，缓存大小，发送超时时间
     * @param registration
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        /*
         * 1. setMessageSizeLimit 设置消息缓存的字节数大小 字节
         * 2. setSendBufferSizeLimit 设置websocket会话时，缓存的大小 字节
         * 3. setSendTimeLimit 设置消息发送会话超时时间，毫秒
         */
        registration.setMessageSizeLimit(10240)
                .setSendBufferSizeLimit(10240)
                .setSendTimeLimit(10000)
                // 添加 WebSocket 用户上、下线监听器
                .addDecoratorFactory(new HttpWebSocketHandlerDecoratorFactory())
        ;
    }

    /**
     * 添加自定义的消息转换器，spring 提供多种默认的消息转换器，
     * 返回false,不会添加消息转换器，返回true，会添加默认的消息转换器，当然也可以把自己写的消息转换器添加到转换链中
     * @param list
     * @return
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> list) {
        return true;
    }

    /**
     * 自定义控制器方法的参数类型，有兴趣可以百度google HandlerMethodArgumentResolver这个的用法
     * @param list
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {

    }

    /**
     * 自定义控制器方法返回值类型，有兴趣可以百度google HandlerMethodReturnValueHandler这个的用法
     * @param list
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }
}
