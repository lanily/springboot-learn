package com.hsiao.springboot.logback.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * 日志注解类
 *
 * @author xiao
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD}) // 只能在方法上使用此注解
public @interface WebLog {

  /**
   * 日志描述，这里使用了@AliasFor 别名。spring提供的
   *
   * @return
   */
  @AliasFor("desc")
  String value() default "";

  /**
   * 日志描述
   *
   * @return
   */
  @AliasFor("value")
  String desc() default "";

  /**
   * 是否不记录日志
   *
   * @return
   */
  boolean ignore() default false;

  String name(); // 所调用接口的名称

  boolean toDb() default false; // 标识该条操作日志是否需要持久化存储
}
