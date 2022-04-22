package com.hsiao.springboot.websocket.stomp.auth;


import com.hsiao.springboot.websocket.stomp.model.User;
import java.security.Principal;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: HttpHandshakeHandler
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
public class HttpHandshakeHandler extends DefaultHandshakeHandler {

    /**
     * 用于与正在建立会话过程中的 WebSocket 的用户相关联的方法，可以在此处配置进行关联的用户信息。
     *
     * @param request    握手请求对象
     * @param wsHandler  WebSocket 处理器
     * @param attributes 从 HTTP 握手到与 WebSocket 会话关联的属性
     * @return 对于 WebSocket 的会话的用户或者 null
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        User user = (User) attributes.get("user");
        return user::getUsername;
    }

}
