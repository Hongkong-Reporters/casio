package com.report.casio.config.context;

import com.report.casio.config.ConsumerConfig;
import com.report.casio.config.ProviderConfig;
import com.report.casio.config.RegistryConfig;
import com.report.casio.config.ServiceConfig;

import java.util.ArrayList;
import java.util.List;

public class RpcConfigContext {
    private final List<RegistryConfig> registryConfigs;
    private ProviderConfig providerConfig;
    private ConsumerConfig consumerConfig;
    private final List<ServiceConfig> serviceConfigs;

    protected RpcConfigContext() {
        registryConfigs = new ArrayList<>();
        serviceConfigs = new ArrayList<>();
    }

    public List<RegistryConfig> getRegistryConfigs() {
        return registryConfigs;
    }

    public ProviderConfig getProviderConfig() {
        return providerConfig;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public List<ServiceConfig> getServiceConfigs() {
        return serviceConfigs;
    }

    protected void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    protected void setProviderConfig(ProviderConfig providerConfig) {
        this.providerConfig = providerConfig;
    }

    protected void addRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfigs.add(registryConfig);
    }

    protected void addServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfigs.add(serviceConfig);
    }

}
