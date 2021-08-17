package com.report.casio.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcClientProxy implements RpcProxy, InvocationHandler {

    @Override
    public <T> T getProxy(Class<T> clazz) {
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

}
