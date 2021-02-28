/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: ConfigUtil Author: xiao Date: 2020/11/14 21:58
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.cache.util;

import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

/**
 * 〈一句话功能简述〉<br>
 * application.properties配置帮助类
 *
 * @projectName springboot-parent
 * @title: ConfigUtil
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
public class ConfigUtil {

  /**
   * 根据配置键读取配置文件的配置
   *
   * @param configKey
   * @return String
   */
  public static String getConfigVal(String configKey) {
    String val = null;
    if (StringUtils.isEmpty(configKey) == true) {
      return val;
    }

    try {
      org.springframework.core.io.Resource resource =
          new ClassPathResource("/config/application.properties");
      Properties properties = PropertiesLoaderUtils.loadProperties(resource);

      val = properties.getProperty(configKey);

    } catch (Exception e) {
      System.out.println(e.toString());
    }

    return val;
  }

}
