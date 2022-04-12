package com.hsiao.springboot.websocket.netty.config;


import com.hsiao.springboot.websocket.netty.annotation.OnBinary;
import com.hsiao.springboot.websocket.netty.annotation.OnClose;
import com.hsiao.springboot.websocket.netty.annotation.OnError;
import com.hsiao.springboot.websocket.netty.annotation.OnEvent;
import com.hsiao.springboot.websocket.netty.annotation.OnMessage;
import com.hsiao.springboot.websocket.netty.annotation.OnOpen;
import com.hsiao.springboot.websocket.netty.annotation.PathVariable;
import com.hsiao.springboot.websocket.netty.annotation.ServerEndpoint;
import com.hsiao.springboot.websocket.netty.server.Session;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注解说明：
 * 注意以下注解都是在com.hsiao.*的包下，不要错误引入成javax.websocket包的注解。
 * @Component也不必添加，WebSocketNettyServer不是单例类，每个session连接共享一个WebSocketNettyServer实例对象。关于session的管理可以参考之前代码中的WsSessionManager类。
 *
 * @ServerEndpoint
 * 当ServerEndpointExporter类通过Spring配置进行声明并被使用，它将会去扫描带有@ServerEndpoint注解的类 被注解的类将被注册成为一个WebSocket端点 所有的配置项都在这个注解的属性中 ( 如:@ServerEndpoint("/ws") )
 * 更多属性配置：
 *
 * https://gitee.com/Yeauty/netty-websocket-spring-boot-starter/tree/master
 *
 * @BeforeHandshake
 * 当有新的连接进入时，对该方法进行回调 注入参数的类型:Session、HttpHeaders…
 *
 * @OnOpen
 * 当有新的WebSocket连接完成时，对该方法进行回调 注入参数的类型:Session、HttpHeaders…
 *
 * @OnClose
 * 当有WebSocket连接关闭时，对该方法进行回调 注入参数的类型:Session
 *
 * @OnError
 * 当有WebSocket抛出异常时，对该方法进行回调 注入参数的类型:Session、Throwable
 *
 * @OnMessage
 * 当接收到字符串消息时，对该方法进行回调 注入参数的类型:Session、String
 *
 * @OnBinary
 * 当接收到二进制消息时，对该方法进行回调 注入参数的类型:Session、byte[]
 *
 * @OnEvent
 * 当接收到Netty的事件时，对该方法进行回调 注入参数的类型:Session、Object
 * @projectName springboot-parent
 * @title: WebSocketNettyServer
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
//@ServerEndpoint(prefix = "ws")
@ServerEndpoint(path = "/ws/{userId}", host = "${ws.host}", port = "${ws.port}")
//@ServerEndpoint(path = "${ws.arg}", host = "${ws.host}", port = "${ws.port}")
//@ServerEndpoint(path = "/ws", host = "${ws.host}", port = "${ws.port}")
//@Component
public class WebSocketNettyServer {
    public static final Logger logger = LoggerFactory.getLogger(WebSocketNettyServer.class);
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketNettyServer> webSocketSet = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 接收userId
    private String userId = "";

    /**
     * 建立ws连接前的配置
     * @param session
     * @param headers
     * @param req
     * @param reqMap
     * @param userId
     * @param pathMap
     */
//    @BeforeHandshake
//    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String userId, @PathVariable Map pathMap){
//        session.setSubprotocols("stomp");
//        if (!"ok".equals(req)){
//            logger.info("Authentication failed!");
//            session.close();
//        }
//
//        logger.info("-----userId----: {}",  userId);
//    }

    /**
     * 连接建立成功调用的方法
     * @param session
     * @param headers
     * @param req
     * @param reqMap
     * @param arg
     * @param pathMap
     */
//    @OnOpen
//    public void onOpen(Session session, HttpHeaders headers,
//            @RequestParam String req,
//            @RequestParam MultiValueMap reqMap,
//            @PathVariable String arg,
//            @PathVariable Map pathMap){
//        logger.info("new connection with req:{}", req);
//    }

    /**
     *
     * @param session
     * @param userId
     */
    @OnOpen
//    public void onOpen(Session session, @PathParam("userId") String userId) {
    public void onOpen(Session session, @PathVariable("userId") String userId) {
        this.session = session;
        // 加入set中
        webSocketSet.add(this);
        // 在线数加1
        addOnlineCount();
        logger.info("有新窗口开始监听:{}, 当前在线人数为:{}", userId, getOnlineCount());
        this.userId = userId;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("one connection closed");
        // 从set中删除
        webSocketSet.remove(this);
        // 在线数减1
        subOnlineCount();
        logger.info("有一连接关闭！当前在线人数为:{}", getOnlineCount());
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 收到客户端消息后调用的方法
     *x
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        logger.info("收到来自窗口:{}的信息:{}", userId, message);
        try {
            this.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        session.sendText("Hello Netty!");
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    logger.info("read idle");
                    break;
                case WRITER_IDLE:
                    logger.info("write idle");
                    break;
                case ALL_IDLE:
                    logger.info("all idle");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 实现服务器主动推送
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.sendText(message);
    }

    /**
     * 发送给指定用户
     * @param message
     * @param userId
     * @throws IOException
     */
    public static void sendToUser(String message, String userId) throws IOException {
        logger.info("推送消息到窗口:{}，推送内容:{}", userId, message);
        for (WebSocketNettyServer item : webSocketSet) {
            try {
                // 这里可以设定只推送给这个sid的，为null则全部推送
                if (userId == null) {
                    item.sendMessage(message);
                } else if (item.userId.equals(userId)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketNettyServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketNettyServer.onlineCount--;
    }
}
