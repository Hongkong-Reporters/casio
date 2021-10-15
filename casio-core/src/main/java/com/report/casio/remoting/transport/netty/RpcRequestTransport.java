package com.report.casio.remoting.transport.netty;

import com.report.casio.domain.RpcRequest;
import com.report.casio.domain.RpcResponse;

import java.util.concurrent.CompletableFuture;

public interface RpcRequestTransport {
    // 发送RpcRequest，Future模式处理
    CompletableFuture<RpcResponse> sendRpcRequest(RpcRequest rpcRequest);

}
