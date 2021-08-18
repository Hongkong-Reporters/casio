package com.report.casio.config;

import com.report.casio.registry.ServiceDiscovery;
import com.report.casio.registry.ServiceRegistry;
import com.report.casio.registry.zookeeper.ZkServiceDiscovery;
import com.report.casio.registry.zookeeper.ZkServiceRegistry;
import com.report.casio.rpc.cluster.loadbalance.LoadBalance;
import com.report.casio.rpc.cluster.loadbalance.RoundLoadBalance;

import java.util.ArrayList;
import java.util.List;

// 启动上下文：包含默认的服务发现类、默认负载均衡算法
// 服务提供者注解扫描注入类
// 写法存在问题，context可见性太高
public class RpcContext {
    private LoadBalance defaultLoadBalance;
    private ServiceDiscovery defaultServiceDiscovery;
    private ServiceRegistry defaultServiceRegistry;
    private List<RegistryConfig> registryConfigs = new ArrayList<>();

    // 测试
    public RpcContext() {
        defaultLoadBalance = new RoundLoadBalance();

        defaultServiceDiscovery = new ZkServiceDiscovery();

        defaultServiceRegistry = new ZkServiceRegistry();

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1");
        registryConfig.setPort(2181);
        registryConfigs.add(registryConfig);

    }

    public LoadBalance getDefaultLoadBalance() {
        return defaultLoadBalance;
    }

    public ServiceDiscovery getDefaultServiceDiscovery() {
        return defaultServiceDiscovery;
    }

    public ServiceRegistry getDefaultServiceRegistry() {
        return defaultServiceRegistry;
    }

    public List<RegistryConfig> getRegistryConfigs() {
        return registryConfigs;
    }
}
