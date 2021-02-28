/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BatchInsertApplication Author: xiao Date: 2020/11/14
 * 13:36 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 〈一句话功能简述〉<br>
 * spring data jpa开启批量插入、批量更新
 *
 * @projectName springboot-parent
 * @title: BatchInsertApplication
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@EnableCaching
@SpringBootApplication
public class BatchInsertApplication {

  public static void main(String[] args) {
    SpringApplication.run(BatchInsertApplication.class, args);
  }
}
