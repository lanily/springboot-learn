package com.hsiao.springboot.websocket.chat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: Message
 * @description: TODO
 * @author xiao
 * @create 2022/4/25
 * @since 1.0.0
 */
@Data
public class Message {
  // 发送者name
  public String from;
  // 接收者name
  public String to;
  // 发送的文本
  public String text;
  // 发送时间
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public Date date;
}
