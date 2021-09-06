package com.report.casio.config.context;

import com.report.casio.config.ConsumerConfig;
import com.report.casio.config.ProviderConfig;
import com.report.casio.config.RegistryConfig;
import com.report.casio.config.ServiceConfig;

import java.util.List;

public class RpcContextFactory {
    private static RpcConfigContext configContext;
    private static final BeanContext beanContext = new BeanContext();

    private RpcContextFactory() {
    }

    public static void createConfigContext(ProviderConfig providerConfig, ConsumerConfig consumerConfig,
                                           List<RegistryConfig> registryConfigs, List<ServiceConfig> serviceConfigs) {
        if (configContext == null) {
            // 饿汉模式双空指针判断
            synchronized (RpcContextFactory.class) {
                configContext = new RpcConfigContext();
                configContext.setProviderConfig(providerConfig);
                configContext.setConsumerConfig(consumerConfig);
                if (registryConfigs != null && !registryConfigs.isEmpty())
                    registryConfigs.forEach(registryConfig -> configContext.addRegistryConfig(registryConfig));
                if (serviceConfigs != null && !serviceConfigs.isEmpty())
                    serviceConfigs.forEach(serviceConfig -> configContext.addServiceConfig(serviceConfig));
            }
        }
    }

    public static RpcConfigContext getConfigContext() {
        return configContext;
    }

    public static BeanContext getBeanContext() {
        return beanContext;
    }
}
