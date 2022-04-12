package com.hsiao.springboot.websocket.dist.task;

import com.hsiao.springboot.websocket.dist.handler.MessagePoster;
import com.hsiao.springboot.websocket.dist.session.DistributeSessionManager;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.web.socket.WebSocketSession;

public class DistributeMessageTask {

    DistributeSessionManager distributeSessionManager;

    MessagePoster messagePoster;

    public DistributeMessageTask(DistributeSessionManager distributeSessionManager,
            MessagePoster messagePoster) {
        this.distributeSessionManager = distributeSessionManager;
        this.messagePoster = messagePoster;
    }


    public void execute() {

        Set<String> localIds = distributeSessionManager.getLocalClientIds();
        for (String id : localIds) {
            if (distributeSessionManager.existsDistributeMessage(id)) {
                WebSocketSession session = distributeSessionManager.getLocalSessions(id);
                List<String> msgList = distributeSessionManager.consumeDistributeMessage(id, 10);
                if (session != null && msgList.size() > 0) {
                    for (String msg : msgList) {
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
