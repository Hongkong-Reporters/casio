package com.report.casio.config;

public class RpcContextFactory {
    private static final RpcContext rpcContext = new RpcContext();

    private RpcContextFactory() {}

    public static RpcContext getRpcContext() {
        return rpcContext;
    }
}
