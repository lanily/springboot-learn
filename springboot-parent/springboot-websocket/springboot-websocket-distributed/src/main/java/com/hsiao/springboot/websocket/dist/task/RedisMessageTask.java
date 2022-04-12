package com.hsiao.springboot.websocket.dist.task;


import com.hsiao.springboot.websocket.dist.handler.MessagePoster;
import com.hsiao.springboot.websocket.dist.session.SessionManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.WebSocketSession;

public class RedisMessageTask {

    SessionManager sessionManager;

    MessagePoster messagePoster;

    String redisKey;

    StringRedisTemplate redisTemplate;

    public RedisMessageTask(SessionManager sessionManager, MessagePoster messagePoster,
            StringRedisTemplate redisTemplate, String redisKey) {
        this.sessionManager = sessionManager;
        this.messagePoster = messagePoster;
        this.redisTemplate = redisTemplate;
        this.redisKey = redisKey;
    }


    public void execute() {
        Set<String> localIds = sessionManager.getAllClientIds();
        for (String id : localIds) {
            if (redisTemplate.hasKey(id)) {
                List<WebSocketSession> sessions = sessionManager.getSession(id);
                List<String> msgList = consumeDistributeMessage(id, 10);
                if (sessions.size() > 0 && msgList.size() > 0) {
                    for (String msg : msgList) {
                        for (WebSocketSession session : sessions) {
                            try {
                                messagePoster.sendMessage(session, msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }

    private List<String> consumeDistributeMessage(String id, int size) {
        List<String> msgList = new ArrayList<>();
        String key = redisKey + id;
        for (int i = 0; i < size; i++) {
            ListOperations<String, String> list = redisTemplate.opsForList();
            String msg = list.leftPop(key);
            if (msg != null) {
                msgList.add(msg);
            }
        }

        if (size > 0 && msgList.size() <= 0) {
            redisTemplate.delete(key);
        }

        return msgList;
    }


}
