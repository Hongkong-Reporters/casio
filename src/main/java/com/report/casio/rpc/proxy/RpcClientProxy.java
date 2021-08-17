package com.report.casio.rpc.proxy;

import com.report.casio.domain.RpcRequest;
import com.report.casio.remoting.transport.netty.client.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class RpcClientProxy implements RpcProxy, InvocationHandler {
    private final NettyClient nettyClient;
    private Class<?> clazz;

    public RpcClientProxy(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
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
            return nettyClient.sendRpcRequest(request).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return proxy;
    }

}
