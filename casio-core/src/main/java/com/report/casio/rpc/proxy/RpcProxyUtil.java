package com.report.casio.rpc.proxy;

import com.report.casio.remoting.transport.netty.client.NettyClient;

/**
 * @author hujiaofen
 * @since 15/7/2022
 */
public class RpcProxyUtil {
    private static RpcProxy rpcProxy;

    private RpcProxyUtil() {}

    public static void createProxy(NettyClient nettyClient) {
        rpcProxy = new RpcClientProxy(nettyClient);
    }

    public static <T> T getProxy(Class<T> clazz) {
        if (rpcProxy != null) {
            return rpcProxy.getProxy(clazz);
        }
        return null;
    }

}
