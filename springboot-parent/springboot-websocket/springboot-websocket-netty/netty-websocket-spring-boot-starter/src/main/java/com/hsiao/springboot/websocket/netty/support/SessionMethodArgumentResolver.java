package com.hsiao.springboot.websocket.netty.support;

import static com.hsiao.springboot.websocket.netty.pojo.PojoEndpointServer.SESSION_KEY;

import com.hsiao.springboot.websocket.netty.server.Session;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: SessionMethodArgumentResolver
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class SessionMethodArgumentResolver implements MethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Session.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Channel channel, Object object)
      throws Exception {
    Session session = channel.attr(SESSION_KEY).get();
    return session;
  }
}
