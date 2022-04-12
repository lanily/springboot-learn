package com.hsiao.springboot.websocket.netty.support;

import com.hsiao.springboot.websocket.netty.annotation.OnEvent;
import io.netty.channel.Channel;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.MethodParameter;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: EventMethodArgumentResolver
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class EventMethodArgumentResolver implements MethodArgumentResolver {

  private AbstractBeanFactory beanFactory;

  public EventMethodArgumentResolver(AbstractBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getMethod().isAnnotationPresent(OnEvent.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Channel channel, Object object)
      throws Exception {
    if (object == null) {
      return null;
    }
    TypeConverter typeConverter = beanFactory.getTypeConverter();
    return typeConverter.convertIfNecessary(object, parameter.getParameterType());
  }
}
