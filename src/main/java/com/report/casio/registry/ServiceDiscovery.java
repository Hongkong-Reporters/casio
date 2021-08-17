package com.report.casio.registry;

import com.report.casio.domain.RpcRequest;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    InetSocketAddress lookup(RpcRequest rpcRequest);

}
