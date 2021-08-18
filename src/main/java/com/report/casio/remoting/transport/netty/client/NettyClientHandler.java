package com.report.casio.remoting.transport.netty.client;

import com.report.casio.domain.RpcResponse;
import com.report.casio.remoting.transport.netty.client.cache.CompletableRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) msg;
            log.info("client read success response, {}", msg);
            CompletableFuture<RpcResponse> completableFuture = CompletableRequest.get(response.getRequestId());
            completableFuture.complete(response);
        } else {
            log.error("client read error msg, {}", msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("");
        super.exceptionCaught(ctx, cause);
    }

}
