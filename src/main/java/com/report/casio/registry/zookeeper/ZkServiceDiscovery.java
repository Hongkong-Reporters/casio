package com.report.casio.registry.zookeeper;

import com.report.casio.common.utils.StringUtils;
import com.report.casio.config.RegistryConfig;
import com.report.casio.config.RpcContextFactory;
import com.report.casio.domain.RpcRequest;
import com.report.casio.registry.ServiceDiscovery;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ZkServiceDiscovery implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookup(RpcRequest rpcRequest) {
        String path = StringUtils.generateProviderPath(rpcRequest.getServiceName());
        try {
            Set<String> childNode = new HashSet<>();
            for (RegistryConfig registryConfig : RpcContextFactory.getRpcContext().getRegistryConfigs()) {
                childNode.addAll(ZkUtils.getChildNode(registryConfig.getHost(), path));
            }
            String hostname = RpcContextFactory.getRpcContext().getDefaultLoadBalance().select(new ArrayList<>(childNode), rpcRequest.getServiceName());
            if (StringUtils.isBlank(hostname)) {
                return null;
            }
            String[] split = hostname.split(":");
            if (split.length != 2) {
                return null;
            }
            return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
