package com.report.casio.rpc.proxy;

public interface RpcProxy {

    <T> T getProxy(Class<T> clazz);

}
