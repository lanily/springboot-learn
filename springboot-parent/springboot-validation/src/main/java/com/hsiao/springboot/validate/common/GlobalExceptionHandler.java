package com.hsiao.springboot.validate.common;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: GlobalExceptionHandler
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
@RestControllerAdvice
@Slf4j
@AutoConfigurationPackage
public class GlobalExceptionHandler {

    /**
     * 请求方法不支持 异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R httpRequestMethodNotSupportedExceptionHandler(
            HttpRequestMethodNotSupportedException e) {
        return R.success(HttpStatus.BAD_REQUEST.value(), "不支持' " + e.getMethod() + "'请求");
    }

    /**
     * spring-context包里面的异常
     * JavaBean参数校验异常处理
     * JavaBean参数校验错误会抛出 BindException
     * 实体对象前不加@RequestBody注解,单个对象内属性校验未通过抛出的异常类型
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public R methodArguments(BindException e) {
        log.warn("throw BindingException,{}", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return R.error(HttpStatus.BAD_REQUEST.value(), "", collect);
    }


    /**
     * 实体对象前不加@RequestBody注解,校验方法参数或方法返回值时,未校验通过时抛出的异常
     * Validation-api包里面的异常
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    public R methodArguments(ValidationException e) {
        log.warn("throw ValidationException,{}", e);
        return R.error(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
    }

    /**
     * spring-context包里面的异常,实体对象前加@RequestBody注解,抛出的异常为该类异常
     * 方法参数如果带有@RequestBody注解，那么spring mvc会使用RequestResponseBodyMethodProcessor
     * 对参数进行序列化,并对参数做校验
     * RequestBody为 json 的参数校验异常捕获
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R methodArguments(MethodArgumentNotValidException e) {
        log.warn("throw MethodArgumentNotValidException,{}", e);
        return R.error(HttpStatus.BAD_REQUEST.value(),
                e.getBindingResult().getFieldError().getDefaultMessage());
    }


    /**
     * 单个参数校验异常处理
     * 单个参数校验错误会抛出 ConstraintViolationException
     **/
    @ExceptionHandler(ConstraintViolationException.class)
    public R constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return R.success(HttpStatus.BAD_REQUEST.value(), "请求参数错误", collect);
    }

    @ExceptionHandler(Exception.class)
    public R methodArguments(Exception e) {
        log.warn("throw exception,{}", e);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}

