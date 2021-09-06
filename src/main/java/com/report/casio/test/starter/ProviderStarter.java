package com.report.casio.test.starter;

import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.CasioConfigInitHandler;
import com.report.casio.config.ServiceConfig;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.config.parser.AnnotationBeanParser;
import com.report.casio.registry.ServiceRegistry;
import com.report.casio.remoting.transport.netty.server.NettyServer;
import com.report.casio.remoting.transport.netty.server.Server;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ProviderStarter implements CasioStarter {

    @SneakyThrows
    @Override
    public void start() {
        new CasioConfigInitHandler().init();

        String path = RpcContextFactory.getBeanContext().getServiceScanPackage();
        Set<ServiceConfig> serviceConfigs = AnnotationBeanParser.scanRegisterService(path);
        ServiceRegistry serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getDefaultExtension();
        for (ServiceConfig serviceConfig : serviceConfigs) {
            serviceRegistry.register(serviceConfig);
        }
        log.info("service registry success");
        Server server = new NettyServer();
        server.doOpen();
        log.info("server open success");
    }

    public static void main(String[] args) {
        new ProviderStarter().start();
    }

}
