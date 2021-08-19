package com.report.casio.remoting.transport.netty.client;

import com.report.casio.common.utils.ByteUtils;
import com.report.casio.domain.RpcMessage;
import com.report.casio.domain.RpcResponse;
import com.report.casio.remoting.transport.netty.client.cache.CompletableRequest;
import com.report.casio.rpc.protocol.ProtocolConstants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
                rpcResponse = (RpcResponse) ByteUtils.bytesToObject(rpcMessage.getContent());
            } else {
                log.error("client read error msg type, {}", rpcMessage);
                return;
            }
            log.info("client read success response, {}", rpcResponse);
            CompletableFuture<RpcResponse> completableFuture = CompletableRequest.get(rpcResponse.getRequestId());
            completableFuture.complete(rpcResponse);
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
