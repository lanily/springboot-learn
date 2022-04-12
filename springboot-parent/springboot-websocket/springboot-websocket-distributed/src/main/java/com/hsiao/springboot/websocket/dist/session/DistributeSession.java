package com.hsiao.springboot.websocket.dist.session;


import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 分布式session
 * 分布式部署时，先将消息发送（共享）至redis，由注册对应实际session的服务发现并发送消息
 */
public class DistributeSession implements WebSocketSession {

    public static final String key_prefix = "msg:distribute_msg:";

    /**
     * client id
     */
    String id;

    StringRedisTemplate stringRedisTemplate;

    final long msg_alive_days = 7;          //消息存活时间，单位天

    public DistributeSession(String id, StringRedisTemplate stringRedisTemplate) {

        this.id = id;
        this.stringRedisTemplate = stringRedisTemplate;

        //todo: 开启线程，发送分布式消息
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public HttpHeaders getHandshakeHeaders() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Principal getPrincipal() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public String getAcceptedProtocol() {
        return null;
    }

    @Override
    public void setTextMessageSizeLimit(int i) {

    }

    @Override
    public int getTextMessageSizeLimit() {
        return 10000;
    }

    @Override
    public void setBinaryMessageSizeLimit(int i) {

    }

    @Override
    public int getBinaryMessageSizeLimit() {
        return 10000;
    }

    @Override
    public List<WebSocketExtension> getExtensions() {
        return null;
    }

    @Override
    public void sendMessage(WebSocketMessage<?> webSocketMessage) throws IOException {
        String key = key_prefix + id;
        ListOperations<String, String> list = stringRedisTemplate.opsForList();
        list.rightPush(key, (String) webSocketMessage.getPayload());
        stringRedisTemplate.expire(key, msg_alive_days, TimeUnit.DAYS);
    }

    public List<String> getMessage(){
        String key = key_prefix + id;
        ListOperations<String, String> list = stringRedisTemplate.opsForList();
        return list.range(key, 0, 100);
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void close(CloseStatus closeStatus) throws IOException {

    }
}
