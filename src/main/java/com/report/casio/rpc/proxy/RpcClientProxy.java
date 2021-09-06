package com.report.casio.rpc.proxy;

import com.report.casio.common.exception.RpcException;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.domain.RpcRequest;
import com.report.casio.domain.RpcResponse;
import com.report.casio.remoting.transport.netty.RpcRequestTransport;
import com.report.casio.remoting.transport.netty.client.cache.CompletableRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RpcClientProxy implements RpcProxy, InvocationHandler {
    private final RpcRequestTransport transport;
    private Class<?> clazz;
    private final Map<String, Integer> countMap = new ConcurrentHashMap<>();
    private final int retries = RpcContextFactory.getConfigContext().getConsumerConfig().getRetries();

    public RpcClientProxy(RpcRequestTransport transport) {
        this.transport = transport;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        this.clazz = clazz;
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        RpcRequest request = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .serviceName(clazz.getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();
        try {
            RpcResponse rpcResponse;
            countMap.put(request.getRequestId(), 0);
            // 添加超时重试机制，通过get添加返回时间限制实现，可能存在bug
            do {
                rpcResponse = getResult(request);
                if (rpcResponse == null) {
                    CompletableRequest.remove(request.getRequestId());
                }
                Integer count = countMap.get(request.getRequestId());
                if (count >= retries) {
                    break;
                }
                if (count > 0) {
                    log.info(method + "第" + count + "次调用超时");
                }
                synchronized (countMap) {
                    count++;
                    countMap.put(request.getRequestId(), count);
                }
            } while (rpcResponse == null);
            if (rpcResponse == null) {
                throw new RpcException(method + "调用失败，多次调用仍然超时");
            }
            CompletableRequest.remove(rpcResponse.getRequestId());
            return rpcResponse.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxy;
    }

    private RpcResponse getResult(RpcRequest request) {
        try {
            int timeout = RpcContextFactory.getConfigContext().getConsumerConfig().getTimeout();
            return transport.sendRpcRequest(request).get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(request + "调用超时");
        }
        return null;
    }

}
