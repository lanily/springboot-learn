package com.hsiao.springboot.websocket.h5.server;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * WebSocketServer
 *
 * 使用Java提供的@ServerEndpoint注解实现
 *
 * 说明
 * 这里有几个注解需要注意一下，首先是他们的包都在 javax.websocket 下。并不是 spring 提供的，而 jdk 自带的，下面是他们的具体作用。
 *
 * @ServerEndpoint 通过这个 spring boot 就可以知道你暴露出去的 ws 应用的路径，有点类似我们经常用的@RequestMapping。比如你的启动端口是8080，而这个注解的值是ws，那我们就可以通过 ws://127.0.0.1:8080/ws 来连接你的应用
 * @OnOpen 当 websocket 建立连接成功后会触发这个注解修饰的方法，注意它有一个Session 参数
 * @OnClose 当 websocket 建立的连接断开后会触发这个注解修饰的方法，注意它有一个session 参数
 * @OnMessage 当客户端发送消息到服务端时，会触发这个注解修改的方法，它有一个 String 入参表明客户端传入的值
 * @OnError 当 websocket 建立连接时出现异常会触发这个注解修饰的方法，注意它有一个Session 参数
另外一点就是服务端如何发送消息给客户端，服务端发送消息必须通过上面说的 Session 类，通常是在@OnOpen 方法中，当连接成功后把 session 存入 Map 的 value，key 是与 session 对应的用户标识，
当要发送的时候通过 key 获得 session 再发送，这里可以通过session.getBasicRemote().sendText() 来对客户端发送消息。
 *
 * 这就是重点了，核心都在这里。
 *
 * 因为WebSocket是类似客户端服务端的形式(采用ws协议)，那么这里的WebSocketServer其实就相当于一个ws协议的Controller
 *
 * 直接@ServerEndpoint("/websocket/{userId}") 、@Component启用即可，然后在里面实现@OnOpen开启连接，@onClose关闭连接，@onMessage接收消息等方法。
 *
 * 新建一个ConcurrentHashMap webSocketMap 用于接收当前userId的WebSocket，方便IM之间对userId进行推送消息。单机版实现到这里就可以。
 *
 * 集群版（多个ws节点）还需要借助mysql或者redis等进行处理，改造对应的sendMessage方法即可。
 *
 * Websocker注入Bean问题
 * 关于这个问题，可以看最新发表的这篇文章，在参考和研究了网上一些攻略后，项目已经通过该方法注入成功，大家可以参考。
 * 关于controller调用controller/service调用service/util调用service/websocket中autowired的解决方法
 *
 * @Component和@ServerEndpoint关于是否单例模式
 *
 * 1. websocket是原型模式，@ServerEndpoint每次建立双向通信的时候都会创建一个实例，区别于spring的单例模式。
 * 2. Spring的@Component默认是单例模式，请注意，默认 而已，是可以被改变的。
 * 3. 这里的@Component仅仅为了支持@Autowired依赖注入使用，如果不加则不能注入任何东西，为了方便。
 * 4. 什么是prototype 原型模式？ 基本就是你需要从A的实例得到一份与A内容相同，但是又互不干扰的实例B的话，就需要使用原型模式。
 * 5. 关于在原型模式下使用static 的webSocketMap，请注意这是ConcurrentHashMap ，也就是线程安全/线程同步的，而且已经是静态变量作为全局调用，这种情况下是ok的，或者大家如果有顾虑或者更好的想法的化，可以进行改进。 例如使用一个中间类来接收和存放session。
 * 6. 为什么每次都@OnOpen都要检查webSocketMap.containsKey(userId) ，首先了为了代码强壮性考虑，假设代码以及机制没有问题，那么肯定这个逻辑是废的对吧。但是实际使用的时候发现偶尔会出现重连失败或者其他原因导致之前的session还存在，这里就做了一个清除旧session，迎接新session的功能。

 *
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 *
 * https://github.com/moshowgame/spring-cloud-study/tree/master/spring-cloud-study-websocket
 *
 * @projectName springboot-parent
 * @title: WebSocketServer
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**当前在线连接数，静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**用来存放每个客户端对应的 WebSocketServer 对象， concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    /**
     * 接收 userId
     */
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        // int nowOnlineCount = 0;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //加入到map中;
            webSocketMap.put(userId, this);
        } else {
            //加入到map中;
            webSocketMap.put(userId, this);
            // 在线人数加1
            // nowOnlineCount = onelineCount.incrementAndGet();
            addOnlineCount();
        }
        log.info("用户：{} 连接成功，当前在线人数为:{}", userId, getOnlineCount());
        try {
            // 发送消息，检验是否连接成功
            sendMessage(String.format("来自服务器端的提示消息，ID: [%s], 的用户连接成功！", userId));
        } catch (IOException e) {
            log.error("用户:{} 连接失败，网络异常!!!!!!", userId);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 记录当前的在线连接数
        int nowOnlineCount = 0;
        if (webSocketMap.containsKey(userId)) {
            // 移除对应的WebSocket对象， 相应的在线连接数减1
            webSocketMap.remove(userId);
            // nowOnlineCount = onelineCount.decrementAndGet();
            subOnlineCount();
        }
        log.info("用户:{} 断开连接，当前在线人数为:{}", userId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息: {}，报文:{}", userId, message);
        if (!StringUtils.isEmpty(message)) {
            try {
                // 解析发送报文
                // JSONObject jsonObject = JSON.parseObject(message);
                // jsonObject.put("fromUserId", this.userId);
                // String toUserId = jsonObject.getString("toUserId");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonObject = mapper.readTree(message);
                // 追加发送人（防止窜改）
                ((ObjectNode) jsonObject).put("fromUserId", this.userId);
                String toUserId = jsonObject.path("toUserId").asText();
                // 传送给对应toUserId用户的websocket
                if (!StringUtils.isEmpty(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toString());
                } else {
                    log.error("请求的 userId:{}，不在该服务器上", toUserId);
                }
            } catch (Exception e) {
                log.error("解析消息失败！！，异常信息：{}", e.getMessage());
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:{}，原因:{}", this.userId, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 给指定的用户发送消息
     * @param userId  用户
     * @param message 消息内容
     */
    public static void sendToUser(@PathParam("userId") String userId, String message)
            throws IOException {
        log.info("发送消息到：{}，报文：{}", userId, message);
        if (!StringUtils.isEmpty(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(String.format("来自用户：%s 发送的消息，内容：%s", userId, message));
        } else {
            log.info("用户{}，不在线！", userId);
        }
    }

    /**
     * 群发消息，给所有用户发送消息
     *
     * @param message 消息内容
     */
    public static void sendToAll(String message) {
        webSocketMap.values().stream().forEach(webSocketServer -> {
            try {
                webSocketServer.sendMessage(message);
            } catch (IOException e) {
                log.error("群发消息失败，异常信息：{}", e.getMessage());
            }
        });
    }

    /**
     * 定时给所有用户发送消息
     *
     */
    @Scheduled(cron  = "0/2 * * * * ?")
    public static void schedule(){
        webSocketMap.values().stream().forEach(webSocketServer -> {
            try {
                webSocketServer.sendMessage("定时消息");
            } catch (IOException e) {
                log.error("群发消息失败，异常信息：{}", e.getMessage());
            }
        });
    }

    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }
}
