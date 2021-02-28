/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: CachingSetup Author:   xiao Date:     2020/10/24
 * 13:04 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.config;


import static java.util.concurrent.TimeUnit.SECONDS;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

/**
 * 在 CacheManager 使用之前，创建我们配置文件定义的缓存,并声明了缓存策略为10秒。
 * @projectName springboot-parent
 * @title: CachingSetup
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Component
public  class CachingSetup implements JCacheManagerCustomizer {
    @Override
    public void customize(CacheManager cacheManager)
    {
        cacheManager.createCache("people", new MutableConfiguration<>()
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(SECONDS, 10)))
//                .setExpiryPolicyFactory(
//                        CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 5)))
                .setStoreByValue(false)
                .setStatisticsEnabled(true));
    }

}


