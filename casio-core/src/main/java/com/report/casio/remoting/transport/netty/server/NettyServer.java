package com.report.casio.remoting.transport.netty.server;

import com.report.casio.common.utils.SystemUtil;
import com.report.casio.remoting.transport.netty.codec.RpcMessageDecoder;
import com.report.casio.remoting.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyServer implements Server {
    private final ServerBootstrap bootstrap;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private Channel channel;

    private final String host;
    private final int port;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.bootstrap = new ServerBootstrap();
        this.bossGroup = SystemUtil.isLinux() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.workGroup = SystemUtil.isLinux() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        bootstrap.group(bossGroup, workGroup)
                .channel(SystemUtil.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        // 心跳机制
                        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                        pipeline.addLast(new RpcMessageEncoder());
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new NettyServerHandler());
                    }
                });
    }

    @Override
    public void doOpen() {
        try {
            ChannelFuture future = bootstrap.bind(host, port).sync();
            // sync表示同步，线程会阻塞在此处
            this.channel = future.channel();
            this.channel.closeFuture().sync();
        } catch (InterruptedException exception) {
            log.error("netty server open failed");
        } finally {
            doClose();
        }
    }

    @Override
    public void doClose() {
        if (bossGroup != null) bossGroup.shutdownGracefully();
        if (workGroup != null) workGroup.shutdownGracefully();
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
