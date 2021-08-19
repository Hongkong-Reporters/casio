package com.report.casio.config;

import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.common.utils.AnnScanUtils;
import com.report.casio.registry.ServiceDiscovery;
import com.report.casio.registry.ServiceRegistry;
import com.report.casio.rpc.cluster.loadbalance.LoadBalance;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 启动上下文：包含默认的服务发现类、默认负载均衡算法
// 服务提供者注解扫描注入类
// 写法存在问题，context可见性太高
public class RpcContext {
    private LoadBalance defaultLoadBalance;
    private ServiceDiscovery defaultServiceDiscovery;
    private ServiceRegistry defaultServiceRegistry;
    private List<RegistryConfig> registryConfigs = new ArrayList<>();
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    // 测试
    @SneakyThrows
    public RpcContext() {
        defaultLoadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getDefaultExtension();

        defaultServiceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getDefaultExtension();

        defaultServiceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getDefaultExtension();

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1");
        registryConfig.setPort(2181);
        registryConfigs.add(registryConfig);

        try {
            for (ServiceConfig serviceConfig : AnnScanUtils.scanRegisterService("com.report.casio")) {
                beanMap.putIfAbsent(serviceConfig.getServiceName(), Class.forName(serviceConfig.getRef()).newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public Object getBean(String serviceName) {
        return beanMap.get(serviceName);
    }

}
