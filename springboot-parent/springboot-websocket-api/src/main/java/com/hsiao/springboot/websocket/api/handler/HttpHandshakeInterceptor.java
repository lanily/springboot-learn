package com.hsiao.springboot.websocket.api.handler;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 *
 * WebSocket拦截器----握手之前将登陆用户信息从session设置到WebSocketSession
 *
 *
 * 说明
 * 通过实现 HandshakeInterceptor 接口来定义握手拦截器，注意这里与上面 Handler 的事件是不同的，这里是建立握手时的事件，分为握手前与握手后，而Handler 的事件是在握手成功后的基础上建立 socket 的连接。
 *
 * 所以在如果把认证放在这个步骤相对来说最节省服务器资源。它主要有两个方法beforeHandshake 与 afterHandshake ，顾名思义一个在握手前触发，一个在握手后触发。
 *
 * 链接：https://juejin.cn/post/6844903976727494669
 *
 * @projectName springboot-parent
 * @title: HttpHandshakeInterceptor
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        logger.info("握手开始");
        // 如果参数中带token，获得请求参数token
        HashMap<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), "utf-8");
        String uid = paramMap.get("token");
        if (StrUtil.isNotBlank(uid)) {
            // 放入属性域
            attributes.put("token", uid);
            logger.info("用户 token : {} 握手成功！", uid);
            return true;
        }

        // 将登陆用户信息从session设置到WebSocketSession
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                // 使用userName区分WebSocketHandler，以便定向发送消息
                // 一般直接保存user实体
                String user = (String) session.getAttribute("SESSION_USERNAME");
                if (user!=null) {
                    attributes.put("WEBSOCKET_USERID", user);
                }
            }
        }
        logger.info("用户登录已失效");
        return false;
    }

    /**
     * 握手后
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        logger.info("握手完成");
    }

}
