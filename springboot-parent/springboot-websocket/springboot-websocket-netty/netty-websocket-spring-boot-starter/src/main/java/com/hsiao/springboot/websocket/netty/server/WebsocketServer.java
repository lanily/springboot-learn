package com.hsiao.springboot.websocket.netty.server;

import com.hsiao.springboot.websocket.netty.handler.HttpServerHandler;
import com.hsiao.springboot.websocket.netty.pojo.PojoEndpointServer;
import com.hsiao.springboot.websocket.netty.util.SslUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import org.springframework.util.StringUtils;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: WebsocketServer
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class WebsocketServer {

    private final PojoEndpointServer pojoEndpointServer;

    private final ServerEndpointConfig config;

    private static final InternalLogger logger =
            InternalLoggerFactory.getInstance(WebsocketServer.class);

    public WebsocketServer(
            PojoEndpointServer webSocketServerHandler, ServerEndpointConfig serverEndpointConfig) {
        this.pojoEndpointServer = webSocketServerHandler;
        this.config = serverEndpointConfig;
    }

    /**
     * 自定义启动方法
     */
    public void init() throws InterruptedException, SSLException {
        EventExecutorGroup eventExecutorGroup = null;
        final SslContext sslCtx;
        if (!StringUtils.isEmpty(config.getKeyStore())) {
            sslCtx =
                    SslUtils.createSslContext(
                            config.getKeyPassword(),
                            config.getKeyStore(),
                            config.getKeyStoreType(),
                            config.getKeyStorePassword(),
                            config.getTrustStore(),
                            config.getTrustStoreType(),
                            config.getTrustStorePassword());
        } else {
            sslCtx = null;
        }
        String[] corsOrigins = config.getCorsOrigins();
        Boolean corsAllowCredentials = config.getCorsAllowCredentials();
        final CorsConfig corsConfig = createCorsConfig(corsOrigins, corsAllowCredentials);

        if (config.isUseEventExecutorGroup()) {
            eventExecutorGroup =
                    new DefaultEventExecutorGroup(
                            config.getEventExecutorGroupThreads() == 0
                                    ? 16
                                    : config.getEventExecutorGroupThreads());
        }
        // 设置boos线程组
        EventLoopGroup boss = new NioEventLoopGroup(config.getBossLoopGroupThreads());
        // 设置work线程组
        EventLoopGroup worker = new NioEventLoopGroup(config.getWorkerLoopGroupThreads());
        // 创建启动助手
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventExecutorGroup finalEventExecutorGroup = eventExecutorGroup;
        bootstrap
                // bossGroup辅助客户端的tcp连接请求, workGroup负责与客户端之前的读写操作
                .group(boss, worker)
                // 设置NIO类型的channel
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeoutMillis())
                .option(ChannelOption.SO_BACKLOG, config.getSoBacklog())
                .childOption(ChannelOption.WRITE_SPIN_COUNT, config.getWriteSpinCount())
                .childOption(
                        ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(
                                config.getWriteBufferLowWaterMark(),
                                config.getWriteBufferHighWaterMark()))
                .childOption(ChannelOption.TCP_NODELAY, config.isTcpNodelay())
                .childOption(ChannelOption.SO_KEEPALIVE, config.isSoKeepalive())
                .childOption(ChannelOption.SO_LINGER, config.getSoLinger())
                .childOption(ChannelOption.ALLOW_HALF_CLOSURE, config.isAllowHalfClosure())
                .handler(new LoggingHandler(LogLevel.DEBUG))
                // 连接到达时会创建一个通道
                .childHandler(
                        // 通道初始化对象
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) {
                                // 流水线管理通道中的处理程序（Handler），用来处理业务
                                ChannelPipeline pipeline = ch.pipeline();
                                // 对wss的处理
                                if (sslCtx != null) {
                                    pipeline.addFirst(sslCtx.newHandler(ch.alloc()));
                                }
                                // 对http协议的支持. webSocket协议本身是基于http协议的，所以这边也要使用http编解码器
                                pipeline.addLast(new HttpServerCodec());
                                /*
                                说明：
                                post请求分三部分. request line / request header / message body
                                HttpObjectAggregator将多个信息转化成单一的request或者response对象
                                1、http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合
                                2、这就是为什么，当浏览器发送大量数据时，就会发送多次http请求
                                */
                                pipeline.addLast(new HttpObjectAggregator(65536));
                                // 对大数据流的支持， 以块的方式来写的处理器
                                pipeline.addLast(new ChunkedWriteHandler());
                                // 对cors的支持
                                if (corsConfig != null) {
                                    pipeline.addLast(new CorsHandler(corsConfig));
                                }
                                pipeline.addLast(
                                        new HttpServerHandler(
                                                pojoEndpointServer, config, finalEventExecutorGroup,
                                                corsConfig != null));
                            }
                        });

        if (config.getSoRcvbuf() != -1) {
            bootstrap.childOption(ChannelOption.SO_RCVBUF, config.getSoRcvbuf());
        }

        if (config.getSoSndbuf() != -1) {
            bootstrap.childOption(ChannelOption.SO_SNDBUF, config.getSoSndbuf());
        }

        // 绑定ip和端口启动服务端
        ChannelFuture channelFuture;
        if ("0.0.0.0".equals(config.getHost())) {
            channelFuture = bootstrap.bind(config.getPort());
        } else {
            try {
                // 绑定netty的启动端口
                channelFuture =
                        bootstrap.bind(
                                new InetSocketAddress(InetAddress.getByName(config.getHost()),
                                        config.getPort()));
            } catch (UnknownHostException e) {
                // 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
//              channelFuture = bootstrap.bind().sync();
                channelFuture = bootstrap.bind(config.getHost(), config.getPort());
                e.printStackTrace();
            }
        }

        // 对关闭通道进行监听
        channelFuture.addListener(
                future -> {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                });

        // 添加钩子释放资源
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    boss.shutdownGracefully().syncUninterruptibly();
                                    worker.shutdownGracefully().syncUninterruptibly();
                                }));
    }

    private CorsConfig createCorsConfig(String[] corsOrigins, Boolean corsAllowCredentials) {
        if (corsOrigins.length == 0) {
            return null;
        }
        CorsConfigBuilder corsConfigBuilder = null;
        for (String corsOrigin : corsOrigins) {
            if ("*".equals(corsOrigin)) {
                corsConfigBuilder = CorsConfigBuilder.forAnyOrigin();
                break;
            }
        }
        if (corsConfigBuilder == null) {
            corsConfigBuilder = CorsConfigBuilder.forOrigins(corsOrigins);
        }
        if (corsAllowCredentials != null && corsAllowCredentials) {
            corsConfigBuilder.allowCredentials();
        }
        corsConfigBuilder.allowNullOrigin();
        return corsConfigBuilder.build();
    }

    public PojoEndpointServer getPojoEndpointServer() {
        return pojoEndpointServer;
    }
}
