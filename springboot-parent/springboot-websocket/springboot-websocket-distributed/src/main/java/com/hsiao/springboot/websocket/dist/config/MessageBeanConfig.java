import com.hsiao.springboot.websocket.dist.config.MessageConfig;
import com.hsiao.springboot.websocket.dist.handler.MessageContext;
import com.hsiao.springboot.websocket.dist.handler.MessageDispatcher;
import com.hsiao.springboot.websocket.dist.handler.MessageHandler;
import com.hsiao.springboot.websocket.dist.handler.MessagePoster;
import com.hsiao.springboot.websocket.dist.session.DistributeSessionManager;
import com.hsiao.springboot.websocket.dist.session.MultiSessionManager;
import com.hsiao.springboot.websocket.dist.session.SessionManager;
import com.hsiao.springboot.websocket.dist.session.SingleSessionManager;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.DispatcherServlet;

@ComponentScan("com.hsiao.springboot.websocket.dist.config")
@EnableConfigurationProperties({com.hsiao.springboot.websocket.dist.config.MessageConfig.class})
public class MessageBeanConfig {


    @Resource
    ApplicationContext applicationContext;

    @Resource
    MessageConfig messageConfig;

    @Bean
    public SessionManager sessionManager() {
        String mode = messageConfig.getSessionMode();
        if ("single".equals(mode)) {
            return new SingleSessionManager();
        } else if ("multi".equals(mode)) {
            return new MultiSessionManager();
        } else if ("distribute".equals(mode)) {
            return new DistributeSessionManager(
                    applicationContext.getBean(StringRedisTemplate.class));
        } else {
            return new SingleSessionManager();
        }
    }

    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher(applicationContext.getBean(DispatcherServlet.class));
    }

    @Bean
    public MessagePoster messageServer(
            SessionManager sessionManager) {
        return new MessagePoster(sessionManager);
    }

    @Bean
    public MessageContext messageContext(SessionManager sessionManager,
            MessageDispatcher messageDispatcher, MessagePoster messagePoster) {
        return new MessageContext(messageConfig, sessionManager, messageDispatcher, messagePoster);
    }


    @Bean
    public MessageHandler messageHandler(MessageContext messageContext) {
        return new MessageHandler(messageContext);
    }


}
