/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: AppConst Author:   xiao Date:     2020/11/14 21:36
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.cache;


/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AppConst
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
public class AppConst {

    public static final String BASE_PACKAGE_NAME = "com.hsiao.springboot.cache";
    public static final int CACHE_MAXIMUM_SIZE = 100;
    public static final long CACHE_MINUTE = 6000;
    public static final String CACHE_VERSION_KEY = "cacheVersion";
    //每页记录数
    public static final int RecordCount = 20;

    //最大重试次数
    public static final int MaxRetryCount = 3;

    //成功
    public static final int Success = 0;

    //失败
    public static final int Fail = -1;
}
