package com.report.casio.remoting.transport.netty.server;

import com.report.casio.common.utils.ByteUtils;
import com.report.casio.domain.RpcMessage;
import com.report.casio.domain.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("server active success");
        super.channelActive(ctx);
    }

    @SneakyThrows
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            RpcRequest request = (RpcRequest) ByteUtils.bytesToObject(rpcMessage.getContent());
            log.info("server read success request, {}", request);
        } else {
            log.info("server read error msg, {}", msg);
        }
    }

    @Override
    // 处理心跳机制
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("");
        super.exceptionCaught(ctx, cause);
    }

}
