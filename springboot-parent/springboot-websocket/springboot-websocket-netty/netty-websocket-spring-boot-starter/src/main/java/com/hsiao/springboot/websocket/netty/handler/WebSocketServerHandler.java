package com.hsiao.springboot.websocket.netty.handler;

import com.hsiao.springboot.websocket.netty.pojo.PojoEndpointServer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: WebSocketServerHandler
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  private final PojoEndpointServer pojoEndpointServer;

  public WebSocketServerHandler(PojoEndpointServer pojoEndpointServer) {
    this.pojoEndpointServer = pojoEndpointServer;
  }

  /**
   * 服务器接受客户端的数据信息
   * @param ctx
   * @param msg
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
    handleWebSocketFrame(ctx, msg);
  }

  /**
   *
   * @param ctx
   * @param cause
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    pojoEndpointServer.doOnError(ctx.channel(), cause);
  }

  /**
   * 客户端与服务器关闭连接时触发
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    pojoEndpointServer.doOnClose(ctx.channel());
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    pojoEndpointServer.doOnEvent(ctx.channel(), evt);
  }

  private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    if (frame instanceof TextWebSocketFrame) {
      pojoEndpointServer.doOnMessage(ctx.channel(), frame);
      return;
    }
    if (frame instanceof PingWebSocketFrame) {
      ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
      return;
    }
    if (frame instanceof CloseWebSocketFrame) {
      ctx.writeAndFlush(frame.retainedDuplicate()).addListener(ChannelFutureListener.CLOSE);
      return;
    }
    if (frame instanceof BinaryWebSocketFrame) {
      pojoEndpointServer.doOnBinary(ctx.channel(), frame);
      return;
    }
    if (frame instanceof PongWebSocketFrame) {
      return;
    }
  }
}
