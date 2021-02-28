/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: LogAspect Author: xiao Date: 2020/11/14 21:14
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.logback.config;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: LogAspect
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
  @Pointcut("execution(public * com.hsiao.springboot.*.*controller.*(..))")
  public void logPointcut() {}

  @Before("logPointcut()")
  public void methodBefore(JoinPoint joinPoint) {
    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    // 打印请求内容
    log.info("---------------请求内容---------------");
    log.info("请求地址:" + request.getRequestURL().toString());
    log.info("请求方式:" + request.getMethod());
    log.info("请求类方法:" + joinPoint.getSignature().getName());
    log.info("请求类方法参数:" + Arrays.toString(joinPoint.getArgs()));
    log.info("---------------请求内容---------------");
  }

  @AfterReturning(returning = "o", pointcut = "logPointcut()")
  public void methodAfterReturning(Object o) {
    log.info("===============返回内容===============");
    log.info("返回的内容:" + o.toString());
    log.info("===============返回内容===============");
  }

  @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
  public void logThrowing(JoinPoint joinPoint, Throwable e) {
    log.info("***************抛出异常***************");

    log.info("请求类方法:" + joinPoint.getSignature().getName());
    log.info("异常内容:" + e);
    log.info("***************抛出异常***************");
  }
}
