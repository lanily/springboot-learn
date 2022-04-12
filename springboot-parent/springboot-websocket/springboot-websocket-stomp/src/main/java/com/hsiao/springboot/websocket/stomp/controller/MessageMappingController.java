package com.hsiao.springboot.websocket.stomp.controller;


import com.hsiao.springboot.websocket.stomp.constants.WsConstants;
import com.hsiao.springboot.websocket.stomp.constants.WsConstants.Broker;
import com.hsiao.springboot.websocket.stomp.model.MessageRequest;
import com.hsiao.springboot.websocket.stomp.model.MessageResponse;
import com.hsiao.springboot.websocket.stomp.model.WebSocketUser;
import java.security.Principal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * WebSocket的 消息处理
 * 使用 @MessageMapping 接收客户端消息。
 * 使用 @SendTo 发送广播消息。
 * 使用 @SendToUser 反馈客户端发送消息结果。
 * 使用 SimpMessagingTemplate 消息模板发送广播消息和给指定客户端发消息。
 *
 * 通信地址除了握手请求地址最好写完整的地址，其它地址均不用写域名或IP
 * - 握手请求(connect)：http://域名或IP/websocket?token=认证token
 * - 广播订阅地址(subscribe)：/topic/toAll
 * - 个人消息订阅地址(subscribe)：/user/queue/toUser
 * - 发送广播通知(send)：/app/toAll
 * - 发送点对点消息(send)：/app/toUser
 * - 获取消息发送结果(subscribe)：@see SubscribeMappingController /user/queue/msg/result
 *
 * 说明：
 *
 * 1）、消息接口使用@MessageMapping注解，前面讲的配置类@EnableWebSocketMessageBroker注解开启后才能使用这个；
 *
 * 2）、这里稍微提一下，真正线上项目都是把websocket服务做成单独的网关形式，提供rest接口给其他服务调用，达到共用的目的，本项目因为不涉及任何数据库交互，所以直接用@MessageMapping注解，后续完整IM项目接入具体业务后会做一个独立的websocket服务，敬请关注哦！
 *
 * 参考：
 * https://www.cnblogs.com/fulongyuanjushi/p/16102628.html
 * https://www.cnblogs.com/vishun/p/14334142.html
 * https://blog.csdn.net/weixin_40693633/article/details/91512632
 * http://www.mydlq.club/article/86/
 * https://blog.csdn.net/jqsad/article/details/77745379
 * https://github.com/shibd/socket.io.java.server.biz
 *
 * @projectName springboot-parent
 * @title: MessageMappingController
 * @description: TODO
 * @author xiao
 * @create 2022/4/20
 * @since 1.0.0
 */
@Controller // 注册一个controller， websocket的消息处理需要放在Controller下
public class MessageMappingController {

    private static final Logger logger = LoggerFactory.getLogger(MessageMappingController.class);

    /**
     * Spring WebSocket消息发送模板
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //发送广播通知

    /**
     * 发送广播消息（一）
     * @MessageMapping("/all") 接收客户端发来的消息，客户端发送消息地址为：/app/toAll
     * @SendTo("/topic/all") 客户端订阅消息地址为：/topic/toAll
     *
     * @param message
     * @return
     * @throws InterruptedException
     */
    @MessageMapping("/toAll")
    @SendTo("/topic/toAll")
    public MessageResponse sendAll(@Payload MessageRequest message) throws InterruptedException {
        //TODO 业务处理
        String content = message.getContent();
        logger.info("[发送消息]>>>> 内容: {}", content);

        // simulated delay
        Thread.sleep(1000);
        //向客户端发送广播消息（方式二），客户端订阅消息地址为：/topic/notice
//        messagingTemplate.convertAndSend("/topic/notice", msg);
        return new MessageResponse("hello, " + HtmlUtils.htmlEscape(content) + "!");
    }


    /**
     * 发送广播消息（二）
     * -- 说明：
     *       1）、@MessageMapping注解对应客户端的stomp.send('url')；
     *       2）、用法一：要么配合@SendTo("转发的订阅路径")，去掉messagingTemplate，同时return msg来使用，return msg会去找@SendTo注解的路径；
     *       3）、用法二：要么设置成void，使用messagingTemplate来控制转发的订阅路径，且不能return msg，个人推荐这种。
     *  接收客户端发来的消息，客户端发送消息地址为：/app/notice
     *  客户端订阅消息地址为：/topic/notice
     *
     *
     * @param message   完整消息，包含消息头和消息体（即header和body）
     * @param accessor  所有消息头信息
     * @param headers   所有头部值
     * @param principal 登录验证信息
     * @param token     指定头部值，这里指token
     * @param content   消息内容
     */
    @MessageMapping("/notice")
    public void notice(Message message,
            StompHeaderAccessor accessor,
            @Headers Map<String, Object> headers,
            Principal principal,
            @Header(name = "token") String token,
            @Payload String content) {

        logger.info("[完整消息]>>>> message: {}", message.toString());
        logger.info("[所有消息头信息]>>>> accessor: {}", accessor.toString());
        logger.info("[头信息]>>>> headers: {}", headers.toString());
        logger.info("[登录验证信息]>>>> principal: {}", principal.getName());
        logger.info("[token信息]>>>> token: {}", token);
        logger.info("[发送信息]>>>> content: {}", content);

        // 发送消息给客户端
        MessageResponse response = new MessageResponse(
                "hello, " + HtmlUtils.htmlEscape(content) + "!");
        messagingTemplate.convertAndSend(WsConstants.Broker.BROKER_TOPIC + "/notice", response);
    }


    /**
     * 发送点对点消息
     *
     *  @MessageMapping("/toUser")  // 接收客户端发来的消息，客户端发送消息地址为：/app/toUser
     *  @SendToUser("/queue/toUser")    // 向当前发消息客户端（就是自己）发送消息的发送结果，
     *  客户端订阅消息地址为：/user/queue/toUser
     *  接收前端send命令，但是单对单返回
     * @param message
     * @return
     */
    @MessageMapping("/toUser")
    @SendToUser(value = "/queue/toUser", broadcast = false)
    public MessageResponse sendToUser(MessageRequest message) throws InterruptedException {
        //TODO 业务处理
        String content = message.getContent();
        logger.info("[发送消息]>>>> 内容: {}", content);

        // simulated delay
        Thread.sleep(1000);
        return new MessageResponse("hello, " + HtmlUtils.htmlEscape(content) + "!");
    }


    /**
     * 根据ID 把消息推送给指定用户
     * 1. 这里用了 @SendToUser 和 返回值 其意义是可以在发送成功后回执给发送放其信息发送成功
     * 2. 非必须，如果实际业务不需要关心此，可以不用@SendToUser注解，方法返回值为void
     * 3. 这里接收人的参数是用restful风格带过来了，websocket把参数带到后台的方式很多，除了url路径，
     *    还可以在header中封装用@Header或者@Headers去取等多种方式
     * @param userId 消息接收人ID
     * @param message 消息JSON字符串
     * @param headerAccessor
     * @return
     */
    @MessageMapping("/toUser/{userId}")
    @SendToUser(value = "/queue/toUser", broadcast = false)
    public MessageResponse sendToUserWithId(@DestinationVariable String userId,
            MessageRequest message,
            StompHeaderAccessor headerAccessor) throws InterruptedException {
        //TODO 业务处理
        // 这里拿到的user对象是在WebSocketChannelInterceptor拦截器中绑定上的对象
        WebSocketUser user = (WebSocketUser) headerAccessor.getUser();
        logger.info("SendToUser controller 中获取用户登录令牌：" + user.getName()
                + " socketId:" + headerAccessor.getSessionId());

        String content = message.getContent();
        logger.info("[发送消息]>>>> 内容: {}", content);

        // simulated delay
        Thread.sleep(1000);
        return new MessageResponse("hello, " + HtmlUtils.htmlEscape(content) + "!");
    }

    /**
     * 发送点对点消息
     * 接收前端send命令，但是单对单返回
     * @param principal
     * @param messageRequest
     */
    @MessageMapping("/alone")
    public void sendToUserWithTemplate(Principal principal, MessageRequest messageRequest) {
        logger.info("接收客户端：{} 连接", principal.getName());
        //TODO 业务处理
        String to = messageRequest.getTo();
        String content = messageRequest.getContent();
        logger.info("[发送消息]>>>> 内容: {}", content);
        // 可以手动发送， 同样有queue
        // 向指定客户端发送消息，第一个参数Principal.name为前面websocket握手认证通过的用户name（全局唯一的），客户端订阅消息地址为：/user/{userId}/queue/alone
        messagingTemplate.convertAndSendToUser(to, Broker.BROKER_QUEUE + "alone", content);
    }
}
