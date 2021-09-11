package com.hsiao.springboot.inject.util;


import com.hsiao.springboot.inject.service.CacheService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: InjectStaticWithMemberVar
 * @description: TODO
 * @author xiao
 * @create 2021/5/30
 * @since 1.0.0
 */
@Component
public class InjectStaticWithMemberVar {
    @Autowired
    private CacheService cacheService;

    private static CacheService staticCacheService;

    @PostConstruct
    public void init(){
        staticCacheService = cacheService;
    }

    public static void cache() {
        staticCacheService.cache();
    }
}
