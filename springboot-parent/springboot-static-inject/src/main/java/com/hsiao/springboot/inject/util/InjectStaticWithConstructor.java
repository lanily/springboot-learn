package com.hsiao.springboot.inject.util;


import com.hsiao.springboot.inject.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: InjectStaticWithConstructor
 * @description: TODO
 * @author xiao
 * @create 2021/5/30
 * @since 1.0.0
 */
@Component
public class InjectStaticWithConstructor {

    private static CacheService cacheService;

    @Autowired
    public InjectStaticWithConstructor(CacheService cacheService) {
        InjectStaticWithConstructor.cacheService = cacheService;
    }

    public static void cache(){
        cacheService.cache();
    }
}