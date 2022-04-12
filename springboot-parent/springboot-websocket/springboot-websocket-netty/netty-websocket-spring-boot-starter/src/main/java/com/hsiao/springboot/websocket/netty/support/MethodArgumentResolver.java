package com.hsiao.springboot.websocket.netty.support;

import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
/**
 * 〈一句话功能简述〉<br>
 *
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public interface MethodArgumentResolver {

  /**
   * Whether the given {@linkplain MethodParameter method parameter} is supported by this resolver.
   *
   * @param parameter the method parameter to check
   * @return {@code true} if this resolver supports the supplied parameter; {@code false} otherwise
   */
  boolean supportsParameter(MethodParameter parameter);

  @Nullable
  Object resolveArgument(MethodParameter parameter, Channel channel, Object object)
      throws Exception;
}
