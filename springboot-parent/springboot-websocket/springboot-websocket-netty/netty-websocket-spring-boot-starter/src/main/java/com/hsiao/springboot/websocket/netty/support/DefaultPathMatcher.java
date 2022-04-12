package com.hsiao.springboot.websocket.netty.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: DefaultPathMatcher
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class DefaultPathMatcher implements WebSocketPathMatcher {

  private String pattern;

  public DefaultPathMatcher(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public String getPattern() {
    return this.pattern;
  }

  @Override
  public boolean matchAndExtract(QueryStringDecoder decoder, Channel channel) {
    if (!pattern.equals(decoder.path())) {
      return false;
    }
    return true;
  }
}
