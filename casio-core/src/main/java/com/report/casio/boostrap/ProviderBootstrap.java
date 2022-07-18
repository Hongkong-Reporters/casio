package com.report.casio.boostrap;

import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.ProviderConfig;
import com.report.casio.config.ServiceConfig;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.config.parser.AnnotationBeanParser;
import com.report.casio.registry.ServiceRegistry;
import com.report.casio.remoting.transport.netty.server.NettyServer;
import lombok.SneakyThrows;

import java.util.Set;

/**
 * @author hujiaofen
 * @since 15/7/2022
 */
public class ProviderBootstrap implements Bootstrap {

    public final NettyServer nettyServer;

    public ProviderBootstrap(ProviderConfig providerConfig) {
        this.nettyServer = new NettyServer(providerConfig.getAddress(), providerConfig.getPort());
    }

    @SneakyThrows
    @Override
    public void start() {
        Set<ServiceConfig> serviceConfigs = AnnotationBeanParser.scanRegisterService("com.report.casio");
        for (ServiceConfig serviceConfig : serviceConfigs) {
            this.registerService(serviceConfig);
        }
        this.nettyServer.doOpen();
    }

    @Override
    public void close() {
        this.nettyServer.doClose();
    }


    @SneakyThrows
    protected void registerService(ServiceConfig serviceConfig) {
        ServiceRegistry serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getDefaultExtension();
        if (RpcContextFactory.getBeanContext().getBean(serviceConfig.getServiceName()) == null) {
            RpcContextFactory.getBeanContext().registerService(serviceConfig.getServiceName(), Class.forName(serviceConfig.getRef()).newInstance());
            serviceRegistry.register(serviceConfig);
        }
    }
}
