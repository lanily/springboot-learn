package com.hsiao.springboot.inject.util;


import com.hsiao.springboot.inject.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * 〈一句话功能简述〉<br>
 * 工具类注入普通对象在静态方法中使用
 * @projectName springboot-parent
 * @title: InjectStaticWithSetterMethod
 * @description: TODO
 * @author xiao
 * @create 2021/5/27
 * @since 1.0.0
 */
@Component // 声明为spring组件，交由容器管理
public class InjectStaticWithSetterMethod {

    // 添加所需service的私有成员
    private static CacheService cacheService;
    //关键点1 -- 静态初始化一个工具类
    // 直接在方法上加注入
    @Autowired
    public void setCacheService(CacheService cacheService) {
//        this.cacheService = cacheService;
        InjectStaticWithSetterMethod.cacheService = cacheService;
    }

    public CacheService getCacheService() {
        return cacheService;
    }

    public static void cache (){
        cacheService.cache();
    }
}
