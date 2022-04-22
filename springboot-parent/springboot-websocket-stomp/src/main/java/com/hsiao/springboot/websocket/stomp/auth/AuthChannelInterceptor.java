package com.hsiao.springboot.websocket.stomp.auth;


import com.hsiao.springboot.websocket.stomp.model.WebSocketUser;
import com.hsiao.springboot.websocket.stomp.util.JwtUtils;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 *
 *
 * 消息通道拦截器
 * 除此之外，如果需要添加监听，我们的监听类需要实现ChannelInterceptor接口，在 springframework包5.0.7之前这一步我们一般是实现ChannelInterceptorAdapter 抽象类，不过这个类已经废弃了，文档也推荐直接实现接口。
 *
 * 在ChannelInterceptor接口中的preSend能在消息发送前做一些处理，例如可以获取到用户登录的唯一token令牌，这里的令牌是我们业务传递给客户端的，例如用户在登录成功后我们后台生成的一个标识符，客户端在和服务端建立websocket连接的时候，我们可以从消息头中获取到这种业务参数，并做一系列后续处理。
 *
 * @projectName springboot-parent
 * @title: AuthChannelInterceptor
 * @description: TODO
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class AuthChannelInterceptor implements ChannelInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthChannelInterceptor.class);

    /**
     * 连接前监听
     *
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);
// https://segmentfault.com/a/1190000007853460
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            String jwtToken = accessor.getFirstNativeHeader("Auth-Token");
//            log.debug("webSocket token is {}", jwtToken);
//            if (StringUtils.isNotEmpty(jwtToken)) {
//                Map sessionAttributes = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
//                sessionAttributes.put(
//                        CsrfToken.class.getName(), new DefaultCsrfToken("Auth-Token", "Auth-Token", jwtToken));
//                UserAuthenticationToken authToken = tokenService.retrieveUserAuthToken(jwtToken);
//                SecurityContextHolder.getContext().setAuthentication(user);
//                accessor.setUser(authToken);
//            }
//        }

        /**
         * 1. 判断是否为首次连接请求，如果已经连接过，直接返回message
         * 2. 网上有种写法是在这里封装认证用户的信息，本文是在http阶段，websockt 之前就做了认证的封装，所以这里直接取的信息
         */
        //1、判断是否首次连接
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            //2、判断token
            /*
             * 1. 这里获取就是JS stompClient.connect(headers, function (frame){.......}) 中header的信息
             * 2. JS中header可以封装多个参数，格式是{key1:value1,key2:value2}
             * 3. header参数的key可以一样，取出来就是list
             * 4. 样例代码header中只有一个token，所以直接取0位
             */
            List<String> nativeHeader = accessor.getNativeHeader("Authorization");
            if (nativeHeader != null && !nativeHeader.isEmpty()) {
                String token = nativeHeader.get(0);
                /*
                 * 1. 这里直接封装到StompHeaderAccessor 中，可以根据自身业务进行改变
                 * 2. 封装大搜StompHeaderAccessor中后，可以在@Controller / @MessageMapping注解的方法中直接带上StompHeaderAccessor
                 *    就可以通过方法提供的 getUser()方法获取到这里封装user对象
                 * 2. 例如可以在这里拿到前端的信息进行登录鉴权
                 */
//                WebSocketUser user = (WebSocketUser) accessor.getUser();

                if (StringUtils.isNotBlank(token)) {
                    WebSocketUser user = JwtUtils.parseToken(token);
                    //通过token获取用户信息，下方用user来代替
                    if (user != null) {
                        //如果存在用户信息，将用户名赋值，后期发送时，可以指定用户名即可发送到对应用户
                        Principal principal = () -> user.getName();
                        accessor.setUser(principal);
                        return message;
                    }
                }
            }
            return null;
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            System.out.println("用户:" + accessor.getUser() + " 断开连接");
        }
        //不是首次连接，已经登陆成功
        return message;
    }

    /**
     * 在消息发送后立刻调用，boolean值参数表示该调用的返回值
     * @param message
     * @param channel
     * @param sent
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        /*
         * 拿到消息头对象后，我们可以做一系列业务操作
         * 1. 通过getSessionAttributes()方法获取到websocketSession，
         *    就可以取到我们在WebSocketHandshakeInterceptor拦截器中存在session中的信息
         * 2. 我们也可以获取到当前连接的状态，做一些统计，例如统计在线人数，或者缓存在线人数对应的令牌，方便后续业务调用
         */
        HttpSession httpSession = (HttpSession) accessor.getSessionAttributes().get("HTTP_SESSION");

        // 这里只是单纯的打印，可以根据项目的实际情况做业务处理
        log.info("postSend 中获取httpSession key：" + httpSession.getId());

        // 忽略心跳消息等非STOMP消息
        if (accessor.getCommand() == null) {
            return;
        }
        // 根据连接状态做处理，这里也只是打印了下，可以根据实际场景，对上线，下线，首次成功连接做处理
        System.out.println(accessor.getCommand());
        switch (accessor.getCommand()) {
            // 首次连接
            case CONNECT:
                log.info("httpSession key：" + httpSession.getId() + " 首次连接");
                break;
            // 连接中
            case CONNECTED:
                break;
            // 下线
            case DISCONNECT:
                log.info("httpSession key：" + httpSession.getId() + " 下线");
                break;
            default:
                break;
        }
    }

    /**
     * 1. 在消息发送完成后调用，而不管消息发送是否产生异常，在次方法中，我们可以做一些资源释放清理的工作
     * 2. 此方法的触发必须是preSend方法执行成功，且返回值不为null,发生了实际的消息推送，才会触发
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel messageChannel, boolean b,
            Exception e) {

    }

    /**
     * 1. 在消息被实际检索之前调用，如果返回false,则不会对检索任何消息，只适用于(PollableChannels)，
     * 2. 在websocket的场景中用不到
     */
    @Override
    public boolean preReceive(MessageChannel messageChannel) {
        return true;
    }

    /**
     * 1. 在检索到消息之后，返回调用方之前调用，可以进行信息修改，如果返回null,就不会进行下一步操作
     * 2. 适用于PollableChannels，轮询场景
     */
    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {
        return message;
    }

    /**
     * 1. 在消息接收完成之后调用，不管发生什么异常，可以用于消息发送后的资源清理
     * 2. 只有当preReceive 执行成功，并返回true才会调用此方法
     * 2. 适用于PollableChannels，轮询场景
     */
    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel messageChannel,
            Exception e) {
    }

}
