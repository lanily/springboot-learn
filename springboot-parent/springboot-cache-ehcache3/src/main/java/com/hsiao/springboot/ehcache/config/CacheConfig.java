/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: CacheConfig Author:   xiao Date:     2020/10/24 20:38
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.config;


import java.util.Arrays;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * @projectName springboot-parent
 * @title: CacheConfig
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
//@Configuration
//@EnableCaching //same as add to Application class
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Cache bookCache = new ConcurrentMapCache("books");
        cacheManager.setCaches(Arrays.asList(bookCache));
        return cacheManager;
    }
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}

