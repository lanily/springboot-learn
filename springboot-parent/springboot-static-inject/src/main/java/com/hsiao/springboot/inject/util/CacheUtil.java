package com.hsiao.springboot.inject.util;


import com.hsiao.springboot.inject.service.CacheService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 * 工具类注入普通对象在静态方法中使用
 * 使用工具类的时候，我们想在static修饰的方法中，通过注入来调用其他方法，这里就存在问题。
 *
 * 第一：普通工具类是不在spring的管理下，spring不会依赖注入
 *
 * 第二：即便使用@Autowired完成注入，由于工具类是静态方法，只能访问静态变量和方法。但是spring不能直接注入static的。
 *
 * 如：
 *
 * @Autowired
 * private static CacheService cacheService
 * 这样直接注入会是null。为什么spring不能直接注入static变量
 *
 * static成员与类相关，其中static变量及初始化块会在类加载器第一次加载类时初始化和执行。而spring管理的是对象实例的创建和属性注入
 *
 *（初始化了static变量，也就是给该static变量分配内存了）
 *
 * 解决这两个问题
 *
 * 第一：在根据类上加入@Component，使它在spring的管理下，由spring完成该类的初始化和注入
 *
 * 第二：1.我们可以构建一个当前根据类对象的静态变量
 * private static CacheUtil cacheUtil;
 *
 * 2.并且在初始化的时候把当前工具类对象赋值给这个变量
 *
 * @PostConstruct  //该注解的方法在构造函数执行后执行
 *  public void init() {
 *  cacheUtil = this;
 * }
 *
 *
 * 3.此时我们注入其它对象
 *
 *  @Autowired
 *  private CacheService cacheService;
 *
 * 4.通过cacheTool.cacheService就可以调用cacheService的变量和方法了
 *
 * https://www.cnblogs.com/jthr/p/13452534.html
 *
 * 标签解释
 * @Autowired：作用于构建器、属性、方法。按byType自动注入。（@Resource默认按 byName自动注入）
 * @PostConstruct：被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。
 * PostConstruct在构造函数之后执行,
 * init()方法之前执行。
 * （PreDestroy（）方法在destroy()方法执行执行之后执行）
 * @projectName springboot-parent
 * @title: CacheUtil
 * @description: TODO
 * @author xiao
 * @create 2021/5/27
 * @since 1.0.0
 */
@Component ///声明组件 - 让spring初始化的时候在spring上下文创建bean并完成注入
public class CacheUtil {

    // 注意这里非静态
    // 添加所需service的私有成员
    @Autowired
    private CacheService cacheService;
    //关键点1 -- 静态初始化一个工具类
    // 这样是为了在spring初始化之前
    private static CacheUtil cacheUtil;

    // 关键点2 -- 通过@PostConstruct 和 @PreDestroy 方法实现初始化和销毁bean之前进行操作
    //@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。
    // PostConstruct在构造函数之后执行,init()方法之前执行
    @PostConstruct
    public void init() {
        cacheUtil = this;
        cacheUtil.cacheService = this.cacheService;
    }


    public static void cache (){
        cacheUtil.cacheService.cache();
    }
}

