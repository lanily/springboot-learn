/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: WebLogAspect Author: xiao Date: 2020/11/20 21:25
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.logback.config;

import com.google.gson.Gson;
import com.hsiao.springboot.logback.annotation.WebLog;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: WebLogAspect
 * @description: TODO
 * @author xiao
 * @create 2020/11/20
 * @since 1.0.0
 */
@Aspect
@Component
@Order(2)
public class WebLogAspect {

  private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
  /** 访问localhost时打印的IP地址 */
  private static final String IP_LOCALHOST = "0:0:0:0:0:0:0:1";
  /** 声明一个线程，用于记录请求与响应整个周期期间在服务端消耗的时间 */
  private ThreadLocal<Long> startTime = new ThreadLocal<>();

  private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

  /** 以 controller 包下定义的所有请求为切入点 */
  /** 横切点 */
  @Pointcut("execution(public * com.hsiao.springboot.*controller..*.*(..))")
  public void webLog() {}

  /**
   * 在切点之前织入
   *
   * <p>接收请求，并记录数据
   *
   * @param joinPoint
   * @throws Throwable
   */
  @Before("webLog()")
  public void doBefore(JoinPoint joinPoint) throws Throwable {
    // 开始打印请求日志
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();

    // 打印请求相关参数
    logger.info(
        "========================================== Start ==========================================");
    // 打印请求 url
    logger.info("URL            : {}", request.getRequestURL().toString());
    // 打印 Http method
    logger.info("HTTP Method    : {}", request.getMethod());
    // 打印调用 controller 的全路径以及执行方法
    logger.info(
        "Class Method   : {}.{}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName());
    // 打印请求的 IP
    logger.info("IP             : {}", request.getRemoteAddr());
    // 打印请求入参
    logger.info("Request Args   : {}", new Gson().toJson(joinPoint.getArgs()));
    logger.info("----------- WebLogAspect doBefore -----------------------------------------");
    startTime.set(System.currentTimeMillis());
  }

  /**
   * 在切点之前织入
   *
   * <p>接收请求，并记录数据
   *
   * @param joinPoint
   * @throws Throwable
   */
  /**
   * 接收请求，并记录数据
   *
   * @param joinPoint
   * @param webLog
   */
  //    @Before(value = "webLog()&& @annotation(WebLog)")
  public void doBefore(JoinPoint joinPoint, WebLog webLog) throws Throwable {
    // 接收到请求
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes sra = (ServletRequestAttributes) ra;
    HttpServletRequest request = sra.getRequest();
    // 记录请求内容，threadInfo存储所有内容
    Map<String, Object> threadInfo = new HashMap<>();
    logger.info("URL : " + request.getRequestURL());
    threadInfo.put("url", request.getRequestURL());
    logger.info("URI : " + request.getRequestURI());
    threadInfo.put("uri", request.getRequestURI());
    logger.info("HTTP_METHOD : " + request.getMethod());
    threadInfo.put("httpMethod", request.getMethod());
    logger.info("REMOTE_ADDR : " + request.getRemoteAddr());
    threadInfo.put("ip", request.getRemoteAddr());
    logger.info(
        "CLASS_METHOD : "
            + joinPoint.getSignature().getDeclaringTypeName()
            + "."
            + joinPoint.getSignature().getName());
    threadInfo.put(
        "classMethod",
        joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    threadInfo.put("args", Arrays.toString(joinPoint.getArgs()));
    logger.info("USER_AGENT" + request.getHeader("User-Agent"));
    threadInfo.put("userAgent", request.getHeader("User-Agent"));
    logger.info("执行方法：" + webLog.name());
    threadInfo.put("methodName", webLog.name());
    threadLocal.set(threadInfo);
  }

  /**
   * 在切点之后织入
   *
   * @throws Throwable
   */
  @After("webLog()")
  public void doAfter() throws Throwable {
    logger.info(
        "=========================================== End ===========================================");
    // 每个请求之间空一行
    logger.info("");
  }

  /**
   * 环绕 获取执行时间
   *
   * @param proceedingJoinPoint
   * @return
   * @throws Throwable
   */
  @Around("webLog()")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = proceedingJoinPoint.proceed();
    Long takeTime = System.currentTimeMillis() - startTime;
    // 打印出参
    logger.info("Response Args  : {}", new Gson().toJson(result));
    // 执行耗时
    logger.info("Time-Consuming : {} ms", takeTime);
    Map<String, Object> threadInfo = threadLocal.get();
    threadInfo.put("takeTime", takeTime);
    threadLocal.set(threadInfo);
    logger.info("耗时：" + takeTime);

    return result;
  }

  /**
   * 执行成功后处理 在请求响应之后，即请求已经经过controller处理返回后
   *
   * @param webLog
   * @param ret
   * @throws Throwable
   */
  //  @AfterReturning(value = "within(com.hsiao.springboot.logback..*.*)", returning = "rvt")
  //  @AfterReturning(value = "webLog() && @annotation(webLog)", returning = "ret")
  public void doAfterReturning(WebLog webLog, Object ret) throws Throwable {
    logger.info("-----------Start WebLogAspect doAfterReturning ------");
    logger.info("本次处理请求耗费时间 : {}", (System.currentTimeMillis() - startTime.get()));
    logger.info("-----------End WebLogAspect doAfterReturning -------------------------------");
    Map<String, Object> threadInfo = threadLocal.get();
    threadInfo.put("result", ret);
    if (webLog.toDb()) {
      // 插入数据库操作
      // insertResult(threadInfo);
    }
    // 处理完请求，返回内容
    logger.info("RESPONSE : " + ret);
  }

  /**
   * 异常处理
   *
   * @param throwable
   */
  @AfterThrowing(value = "webLog()", throwing = "throwable")
  public void doAfterThrowing(Throwable throwable) {
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();

    ServletRequestAttributes sra = (ServletRequestAttributes) ra;

    HttpServletRequest request = sra.getRequest();
    // 异常信息
    logger.error("{}接口调用异常，异常信息{}", request.getRequestURI(), throwable);
  }
}
