package com.report.casio.remoting.transport.netty.server;

import com.report.casio.common.utils.ByteUtils;
import com.report.casio.config.RpcContextFactory;
import com.report.casio.domain.RpcMessage;
import com.report.casio.domain.RpcRequest;
import com.report.casio.domain.RpcResponse;
import com.report.casio.rpc.protocol.ProtocolConstants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

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
            } else {
                log.info("server read error msg type, {}", rpcMessage);
                return;
            }
            Object service = RpcContextFactory.getRpcContext().getBean(request.getServiceName());
            if (service == null) {
                log.error("service Impl not exist, serviceName: {}", request.getServiceName());
            } else {
                Method method = service.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
                Object result = method.invoke(service, request.getParameters());
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setResult(result);
                rpcResponse.setRequestId(request.getRequestId());
                RpcMessage resMessage = new RpcMessage(rpcResponse);
                ctx.writeAndFlush(resMessage);
                log.info("server read success request {}, and execute success", request);
            }
            long executeTime = System.currentTimeMillis() - startTime;
            if (executeTime > RpcContextFactory.getRpcContext().getProviderConfig().getTimeout()) {
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("");
        super.exceptionCaught(ctx, cause);
    }

}
