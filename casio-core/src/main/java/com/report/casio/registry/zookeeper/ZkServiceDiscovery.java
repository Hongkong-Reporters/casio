package com.report.casio.registry.zookeeper;

import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.common.utils.StringUtil;
import com.report.casio.config.RegistryConfig;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.domain.RpcRequest;
import com.report.casio.registry.ServiceDiscovery;
import com.report.casio.registry.cache.ServiceCacheFactory;
import com.report.casio.rpc.cluster.loadbalance.LoadBalance;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZkServiceDiscovery implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookup(RpcRequest rpcRequest) {
        try {
            String path = StringUtil.generateProviderPath(rpcRequest.getServiceName());
            ServiceCacheFactory cacheFactory = ExtensionLoader.getExtensionLoader(ServiceCacheFactory.class).getDefaultExtension();
            List<String> hosts;
            // 调用缓存访问
            if (cacheFactory.getCache().get(path) != null) {
                hosts = cacheFactory.getCache().get(path);
            } else {
                Set<String> childNode = new HashSet<>();
                for (RegistryConfig registryConfig : RpcContextFactory.getConfigContext().getRegistryConfigs()) {
                    childNode.addAll(ZkUtils.getChildNode(registryConfig.getHost(), path));
                    // 添加watch，如果child node发生变化，删除cache信息
                    ZkUtils.addNodeWatcher(registryConfig.getHost(), path);
                }
                hosts = new ArrayList<>(childNode);
                cacheFactory.getCache().put(path, hosts);
            }
            LoadBalance loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getDefaultExtension();
            String hostname = loadBalance.select(hosts, rpcRequest.getServiceName());
            if (StringUtil.isBlank(hostname)) {
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
