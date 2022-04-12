package com.hsiao.springboot.websocket.dist.session;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.socket.WebSocketSession;

/**
 * 基于redis的分布式session管理器
 */
public class DistributeSessionManager extends SingleSessionManager {

    public static final String KEY_PREFIX = "msg:share:";


    StringRedisTemplate stringRedisTemplate;

    public DistributeSessionManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<WebSocketSession> getSession(String id) {
        String key = KEY_PREFIX + id;
        if(clientSessions.containsKey(id)){
            return super.getSession(id);
        } else if(stringRedisTemplate.hasKey(key)){
            List<WebSocketSession> sessions = new ArrayList<>();
            sessions.add(new DistributeSession(id, stringRedisTemplate));
            return sessions;
        }
        return new ArrayList<>();
    }

    public Set<String> getLocalClientIds(){
        return super.getAllClientIds();
    }

    public List<WebSocketSession> getLocalSessions() {
        return super.getAllSessions();
    }

    public WebSocketSession getLocalSessions(String id) {
        return clientSessions.get(id);
    }

    @Override
    public List<WebSocketSession> getAllSessions() {
        List<WebSocketSession> allSessions = new ArrayList<>();
        allSessions.addAll(super.getAllSessions());

        Set<String> ids = this.getAllClientIds();
        for(String id : ids){
            if(clientSessions.containsKey(id)){
                allSessions.add(new DistributeSession(id, stringRedisTemplate));
            }
        }

        return allSessions;
    }

    @Override
    public Set<String> getAllClientIds() {
        //TODO: 该方式有性能风险，需优化。建议用额外key存储所有id
        return stringRedisTemplate.keys(KEY_PREFIX + "*");
    }

    @Override
    public boolean connect(String id, WebSocketSession session) {
        super.connect(id, session);
        String key = KEY_PREFIX + id;
        //基于redis同步session用户
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(key, session.getId());
        stringRedisTemplate.expire(key, 3600 * 24, TimeUnit.SECONDS);

        return true;
    }


    @Override
    public boolean disconnect(String id) {
        super.disconnect(id);

        String key = KEY_PREFIX + id;
        stringRedisTemplate.delete(key);

        return true;
    }


    public boolean existsDistributeMessage(String id){
        return stringRedisTemplate.hasKey(DistributeSession.key_prefix + id);
    }

    public List<String> consumeDistributeMessage(String id, int size){
        List<String> msgList = new ArrayList<>();
        String key = DistributeSession.key_prefix + id;
        for(int i=0; i<size; i++){
            ListOperations<String, String> list = stringRedisTemplate.opsForList();
            String msg = list.leftPop(key);
            if(msg != null){
                msgList.add(msg);
            }
        }

        if(size > 0 && msgList.size() <= 0){
            stringRedisTemplate.delete(key);
        }

        return msgList;
    }
}
