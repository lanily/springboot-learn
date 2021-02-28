/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: TimeUtilTest Author: xiao Date: 2020/11/14 16:33
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert;

import static org.assertj.core.api.Assertions.assertThat;

import com.hsiao.springboot.batch.insert.util.TimeUtil;
import org.junit.jupiter.api.Test;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: TimeUtilTest
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
public class TimeUtilTest {
  @Test
  public void testToInfluxDBTimeFormatTest() throws Exception {
    assertThat(TimeUtil.toInfluxDBTimeFormat(1477896740020L)).isEqualTo("2016-10-31T06:52:20.020Z");
    assertThat(TimeUtil.toInfluxDBTimeFormat(1477932740005L)).isEqualTo("2016-10-31T16:52:20.005Z");
  }

  @Test
  public void testFromInfluxDBTimeFormatTest() throws Exception {
    assertThat(TimeUtil.fromInfluxDBTimeFormat("2016-10-31T06:52:20.020Z"))
        .isEqualTo(1477896740020L);
    assertThat(TimeUtil.fromInfluxDBTimeFormat("2016-10-31T16:52:20.005Z"))
        .isEqualTo(1477932740005L);
    assertThat(TimeUtil.fromInfluxDBTimeFormat("2016-10-31T16:52:20Z")).isEqualTo(1477932740000L);
    assertThat(TimeUtil.fromInfluxDBTimeFormat("2016-10-31T06:52:20Z")).isEqualTo(1477896740000L);
  }
}
