package com.hsiao.springboot.websocket.stomp.auth;


import com.hsiao.springboot.websocket.stomp.model.User;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 *
 * 依据 HttpSession 实现鉴权
 *
 * WebSocket 握手请求拦截器，可用于拦截握手请求和响应，以及将 HTTP 属性传递到目标 WebSocketHandler，这里主要是设置捂手时候，从 HTTP Session 中获取用户信息，传入 WebSocket 信息中
 *
 * @projectName springboot-parent
 * @title: HttpHandshakeHanlder
 * @description: TODO
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);

    /**
     * 握手前拦截，从 HTTP 中参数传入 WebSocket Attributes 方便后续取出相关参数、
     *
     * @param request 请求对象
     * @param response 响应对象
     * @param wsHandler WebSocket 处理器
     * @param attributes 从 HTTP 握手到与 WebSocket 会话关联的属性
     * @return 如果返回 true 则继续握手，返回 false 则终止握手
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) {
        //这里就是简单
        HttpSession session = getSession(request);
/*        if (session != null) {
//            if (isCopyHttpSessionId()) {
            attributes.put("sessionId", session.getId());
//            }
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
//                if (isCopyAllAttributes() || getAttributeNames().contains(name)) {
                attributes.put(name, session.getAttribute(name));
//                }
            }
        }*/

        if (session != null) {
            // 从 HTTP Session 中获取用户信息
//            WebSocketUser user = (WebSocketUser) session.getAttribute("user");
            User user = (User) session.getAttribute("user");
            if (user != null) {
                // 将从 HTTP Session 中获取的用户信息存入 WebSocket 的 Attributes 对象中
                attributes.put("user", user);
                // 继续握手
                // return true;
            }
        }
        // 终止握手
        return false;
    }

    /**
     * 握手完成后调用
     * @param request 请求对象
     * @param response 响应对象
     * @param wsHandler WebSocket 处理器
     * @param ex 异常信息
     */
    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {

    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            // 将 request 对象转换为 ServletServerHttpRequest 对象
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            // 获取 HTTP Session 对象
//            HttpSession session = serverRequest.getServletRequest().getSession();
            return serverRequest.getServletRequest().getSession(false);
        }
        return null;
    }
}
