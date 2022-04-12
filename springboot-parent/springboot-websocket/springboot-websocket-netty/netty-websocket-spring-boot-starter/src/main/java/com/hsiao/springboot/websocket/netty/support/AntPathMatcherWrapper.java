package com.hsiao.springboot.websocket.netty.support;

import static com.hsiao.springboot.websocket.netty.pojo.PojoEndpointServer.URI_TEMPLATE;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.util.AntPathMatcher;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: AntPathMatcherWrapper
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class AntPathMatcherWrapper extends AntPathMatcher implements WebSocketPathMatcher {

    private String pattern;

    public AntPathMatcherWrapper(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public boolean matchAndExtract(QueryStringDecoder decoder, Channel channel) {
        Map<String, String> variables = new LinkedHashMap<>();
        boolean result = doMatch(pattern, decoder.path(), true, variables);
        if (result) {
            channel.attr(URI_TEMPLATE).set(variables);
            return true;
        }
        return false;
    }
}
