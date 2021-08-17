package com.report.casio.remoting.transport.netty.client;

import com.report.casio.remoting.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class NettyClient implements Client {
    private final Bootstrap bootstrap;
    private final EventLoopGroup workGroup;

    public NettyClient() {
        this.bootstrap = new Bootstrap();
        this.workGroup = new NioEventLoopGroup();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                // 设置客户端调用服务端接口的超时时间5s
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // 心跳机制
                        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                        pipeline.addLast(new RpcMessageEncoder());
//                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public void doConnect() {
        bootstrap.connect();
    }

    @Override
    public void doClose() {
        if (workGroup != null) workGroup.shutdownGracefully();
    }

    @Override
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        return null;
    }

}
