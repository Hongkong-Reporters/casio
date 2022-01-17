package com.report.casio.remoting.transport.netty.server;

import com.report.casio.common.exception.RpcException;
import com.report.casio.common.utils.ByteUtils;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.domain.RpcMessage;
import com.report.casio.domain.RpcRequest;
import com.report.casio.domain.RpcResponse;
import com.report.casio.rpc.protocol.ProtocolConstants;
import com.report.casio.timer.WheelTimerJob;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

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
            long startTime = System.currentTimeMillis();
            RpcMessage rpcMessage = (RpcMessage) msg;
            RpcRequest request;
            if (rpcMessage.getType() == ProtocolConstants.REQUEST_TYPE) {
                request = (RpcRequest) ByteUtils.bytesToObject(rpcMessage.getContent());
            } else if (rpcMessage.getType() == ProtocolConstants.HEARTBEAT) {
                log.info("server receive heart beat, time: " + new Date());
                return;
            } else {
                log.info("server read error msg type, {}", rpcMessage);
                return;
            }
            Object service = RpcContextFactory.getBeanContext().getBean(request.getServiceName());
            if (service == null) {
                String errorMsg = "service Impl not exist, serviceName: " + request.getServiceName();
                log.error(errorMsg);
                RpcException exception = new RpcException(errorMsg);
                exception.setRequestId(request.getRequestId());
                throw exception;
            } else {
                try {
                    Method method = service.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
                    Object result = method.invoke(service, request.getParameters());
                    RpcResponse rpcResponse = new RpcResponse();
                    rpcResponse.setResult(result);
                    rpcResponse.setRequestId(request.getRequestId());
                    RpcMessage resMessage = new RpcMessage(rpcResponse);
                    ctx.writeAndFlush(resMessage);
                    log.info("server read success request {}, and execute success", request);
                } catch (Exception e) {
                    RpcException exception = new RpcException("server run error, msg: " + rpcMessage, e);
                    exception.setRequestId(request.getRequestId());
                    throw exception;
                }
            }
            long executeTime = System.currentTimeMillis() - startTime;
            if (executeTime > RpcContextFactory.getConfigContext().getProviderConfig().getTimeout()) {
                log.warn("server execute timeout, {}", executeTime);
            }
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
        if (cause instanceof RpcException) {
            RpcException exception = (RpcException) cause;
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(exception.getRequestId());
            rpcResponse.setException(exception);
            RpcMessage resMessage = new RpcMessage(rpcResponse);
            ctx.writeAndFlush(resMessage);
        }
    }

}
