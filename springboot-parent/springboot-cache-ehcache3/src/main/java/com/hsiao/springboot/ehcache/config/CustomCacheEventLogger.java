/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EventLogger Author: xiao Date: 2020/10/24 13:03
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache.config;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存模板里配置了日志输入器 EventLogger，用来监听缓存数据变更的事件
 *
 * @projectName springboot-parent
 * @title: EventLogger
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
public class CustomCacheEventLogger implements CacheEventListener<Object, Object> {

  private static final Logger LOG = LoggerFactory.getLogger(CustomCacheEventLogger.class);

  @Override
  public void onEvent(CacheEvent<?, ?> event) {
//    LOG.info(
//        "Event: "
//            + event.getType()
//            + " Key: "
//            + event.getKey()
//            + " old value: "
//            + event.getOldValue()
//            + " new value: "
//            + event.getNewValue());
    LOG.info(
        "Cache event = {}, Key = {},  Old value = {}, New value = {}",
        event.getType(),
        event.getKey(),
        event.getOldValue(),
        event.getNewValue());
  }
}
