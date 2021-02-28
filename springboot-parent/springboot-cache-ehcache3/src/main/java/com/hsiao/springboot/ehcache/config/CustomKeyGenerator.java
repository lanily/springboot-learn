/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: CustomKeyGenerator Author:   xiao Date:
 * 2020/11/14 07:34 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.config;


import java.lang.reflect.Method;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

/**
 * 自定义缓存key生成器
 * @projectName springboot-parent
 * @title: CustomKeyGenerator
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
public class CustomKeyGenerator implements KeyGenerator {
    private static  final Logger LOG = LoggerFactory.getLogger(CustomKeyGenerator.class);
    @Override
    public Object generate(Object target, Method method, Object... params) {
        LOG.info(
            "books key: "
                + target.getClass().getSimpleName()
                + "_"
                + method.getName()
                + "_"
                + StringUtils.arrayToDelimitedString(params, "_"));
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Date) {
                params[i] = new java.sql.Date(((Date) params[i]).getTime());
            }
        }
        return  target.getClass().getSimpleName()
                + "_"
                + method.getName()
                + "_"
                + StringUtils.arrayToDelimitedString(params, "_");
    }
}

