package com.report.casio.remoting.transport.netty.client.cache;

import com.report.casio.domain.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

// 消费者发送数据后，接收生产者处理的结果，存在延迟，使用CompletableFuture和channelRead处理
// key: requestId
public class CompletableRequest {
    private static final Map<String, CompletableFuture<RpcResponse>> COMPLETABLE_REQUEST_MAP = new ConcurrentHashMap<>();

    private CompletableRequest() {}

    public static CompletableFuture<RpcResponse> get(String requestId) {
        return COMPLETABLE_REQUEST_MAP.get(requestId);
    }

    public static void put(String requestId, CompletableFuture<RpcResponse> completableFuture) {
        COMPLETABLE_REQUEST_MAP.putIfAbsent(requestId, completableFuture);
    }

    public static void remove(String request) {
        synchronized (COMPLETABLE_REQUEST_MAP) {
            COMPLETABLE_REQUEST_MAP.remove(request);
        }
    }

}
