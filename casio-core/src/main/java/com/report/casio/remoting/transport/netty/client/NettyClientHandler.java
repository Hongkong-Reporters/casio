package com.report.casio.remoting.transport.netty.client;

import com.report.casio.domain.RpcMessage;
import com.report.casio.domain.RpcResponse;
import com.report.casio.remoting.transport.netty.client.cache.ChannelClient;
import com.report.casio.remoting.transport.netty.client.cache.CompletableRequest;
import com.report.casio.rpc.protocol.ProtocolConstants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("client connect success");
        // 设置最高水位，防止高并发出现内存泄露
        ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
    }

    @SneakyThrows
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            RpcResponse rpcResponse;
            if (rpcMessage.getType() == ProtocolConstants.RESPONSE_TYPE) {
                rpcResponse = rpcMessage.getResponse();
                ChannelClient.get((InetSocketAddress) ctx.channel().remoteAddress()).setLastRead();
            } else if (rpcMessage.getType() == ProtocolConstants.HEARTBEAT) {
                log.info("client receive heart beat, time: " + new Date());
                return;
            } else {
                log.error("client read error msg type, {}", rpcMessage);
                return;
            }
            log.info("client read success response, {}", rpcResponse);
            Throwable exception = rpcResponse.getException();
            if (exception != null) {
                exception.printStackTrace();
            } else {
                CompletableFuture<RpcResponse> completableFuture = CompletableRequest.get(rpcResponse.getRequestId());
                if (completableFuture != null) {
                    completableFuture.complete(rpcResponse);
                }
            }
        } else {
            log.error("client read error msg, {}", msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client exception: ", cause);
    }

}
