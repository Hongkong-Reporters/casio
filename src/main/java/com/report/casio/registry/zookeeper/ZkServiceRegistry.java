package com.report.casio.registry.zookeeper;

import com.report.casio.common.utils.StringUtils;
import com.report.casio.config.RegistryConfig;
import com.report.casio.config.RpcContextFactory;
import com.report.casio.config.ServiceConfig;
import com.report.casio.registry.ServiceRegistry;

import java.net.InetAddress;
import java.util.List;

public class ZkServiceRegistry implements ServiceRegistry {
    @Override
    public void register(ServiceConfig serviceConfig) throws Exception {
        List<RegistryConfig> registryConfigs = RpcContextFactory.getRpcContext().getRegistryConfigs();
        for (RegistryConfig registryConfig : registryConfigs) {
            ZkUtils.create(registryConfig.getHost(),
                    StringUtils.generateProviderPath(serviceConfig.getServiceName(), InetAddress.getLocalHost().getHostAddress() + ":9001"), null);
        }
    }
}
