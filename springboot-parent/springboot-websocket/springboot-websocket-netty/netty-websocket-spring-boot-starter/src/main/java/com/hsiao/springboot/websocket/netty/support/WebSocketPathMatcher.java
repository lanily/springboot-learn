package com.hsiao.springboot.websocket.netty.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: WebSocketPathMatcher
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public interface WebSocketPathMatcher {

  String getPattern();

  boolean matchAndExtract(QueryStringDecoder decoder, Channel channel);
}
