/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeRepositoryTest Author: xiao Date: 2020/11/14
 * 15:59 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert;

import com.alibaba.fastjson.JSONObject;
import com.hsiao.springboot.batch.insert.entity.IndexCalculated;
import com.hsiao.springboot.batch.insert.repository.StockRepository;
import com.hsiao.springboot.batch.insert.service.BatchService;
import com.hsiao.springboot.batch.insert.util.TimeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 〈一句话功能简述〉<br>
 *
 * https://blog.csdn.net/weixin_39792935/article/details/104993301
 * @projectName springboot-parent
 * @title: EmployeeRepositoryTest
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockRepositoryTest {

  @Value("${indexCode}")
  private String indexCode;

  @Resource private StockRepository stockRepository;
  @Resource private BatchService batchService;

  /** 循环写入 */
  @Test
  public void forTest() {
    List<String> indexCodeArr = Arrays.asList(indexCode.split(","));
    Set<String> indexCodeSet = new HashSet<>(indexCodeArr);
    log.info("indexCodeSet:{}", JSONObject.toJSONString(indexCodeSet));
    log.info("indexCodeSe.size():{}", indexCodeSet.size());

    long timeIdStart = System.currentTimeMillis();
    String time = TimeUtil.toDBTimeFormat(timeIdStart);
    int record = 0;
    for (String indexCode : indexCodeSet) {
      record += stockRepository.updateIndexCalculated(indexCode, time);
    }
    log.info("record:{}", record);
    log.info("运行时间,time:{}秒", (System.currentTimeMillis() - timeIdStart) / 1000.0);
  }

  /** 批量写入 */
  @Test
  public void batchTest() {
    List<String> indexCodeArr = Arrays.asList(indexCode.split(","));
    Set<String> indexCodeSet = new HashSet<>(indexCodeArr);
    log.info("indexCodeSet:{}", JSONObject.toJSONString(indexCodeSet));
    log.info("indexCodeSe.size():{}", indexCodeSet.size());

    long timeIdStart = System.currentTimeMillis();
    String time = TimeUtil.toDBTimeFormat(timeIdStart);
    List<IndexCalculated> list = new ArrayList<>();
    for (String indexCode : indexCodeSet) {
      IndexCalculated indexCalculated = new IndexCalculated();
      indexCalculated.setIndexCode(indexCode);
      indexCalculated.setUpdateTime(time);
      list.add(indexCalculated);
    }
    batchService.batchInsert(list);
    //        batchService.batchUpdate(list);
    log.info("运行时间,time:{}秒", (System.currentTimeMillis() - timeIdStart) / 1000.0);
  }
}
