/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: RecordNotFoundException Author:   xiao Date:
 * 2020/11/14 08:22 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 自定义记录找不到异常类
 * @projectName springboot-parent
 * @title: RecordNotFoundException
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends Exception{

    public RecordNotFoundException (String message) {
        super(message);
    }

    public RecordNotFoundException (String message, Throwable t) {
        super(message, t);
    }
}

