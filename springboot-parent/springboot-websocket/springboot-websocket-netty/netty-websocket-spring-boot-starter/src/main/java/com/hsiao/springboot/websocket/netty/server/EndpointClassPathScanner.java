package com.hsiao.springboot.websocket.netty.server;

import com.hsiao.springboot.websocket.netty.annotation.ServerEndpoint;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: EndpointClassPathScanner
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class EndpointClassPathScanner extends ClassPathBeanDefinitionScanner {

  public EndpointClassPathScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
    super(registry, useDefaultFilters);
  }

  @Override
  protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
    addIncludeFilter(new AnnotationTypeFilter(ServerEndpoint.class));
    return super.doScan(basePackages);
  }
}
