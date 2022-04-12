package com.hsiao.springboot.websocket.netty.support;

import com.hsiao.springboot.websocket.netty.annotation.OnMessage;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.core.MethodParameter;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: TextMethodArgumentResolver
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class TextMethodArgumentResolver implements MethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getMethod().isAnnotationPresent(OnMessage.class)
        && String.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Channel channel, Object object)
      throws Exception {
    TextWebSocketFrame textFrame = (TextWebSocketFrame) object;
    return textFrame.text();
  }
}
