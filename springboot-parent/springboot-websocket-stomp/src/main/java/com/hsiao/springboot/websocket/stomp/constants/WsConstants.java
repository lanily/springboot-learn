package com.hsiao.springboot.websocket.stomp.constants;


/**
 *
 * websocket 常量
 *
 *  订阅常量类
 * 后面的websocket配置类会用到这几个常量
 *
 * stomp端点地址： 连接websocket时的后缀地址，比如127.0.0.1:8888/websocket。
 *
 * websocket前缀：前端调服务端消息接口时的URL都加上了这个前缀，比如默认是/send，变成/app/send。
 *
 * 点对点代理地址：如果websocket配置类中设置了代理路径，一般点对点订阅路径喜欢用/queue。
 *
 * 广播代理地址：如果websocket配置类中设置了代理路径，一般广播订阅路径喜欢用这个/topic
 *
 * @projectName springboot-parent
 * @title: WsConstants
 * @description: TODO
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
public class WsConstants {

    /**stomp端点地址**/
    public static final String WS_PATH = "/ws";
    /** websocket前缀**/
    public static final String WS_APP_PREFIX = "/app";
    public static final String WS_USER_PREFIX = "/user";

    /**
     * 消息订阅地址常量
     */
    public static final class Broker {

        // 点对点消息代理地址
        public static final String BROKER_QUEUE = "/queue/";
        // 广播消息代理地址
        public static final String BROKER_TOPIC = "/topic";

    }

}
