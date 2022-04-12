package com.hsiao.springboot.websocket.netty.support;

import com.hsiao.springboot.websocket.netty.annotation.OnError;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: ThrowableMethodArgumentResolver
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class ThrowableMethodArgumentResolver implements MethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getMethod().isAnnotationPresent(OnError.class)
        && Throwable.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Channel channel, Object object)
      throws Exception {
    if (object instanceof Throwable) {
      return object;
    }
    return null;
  }
}
