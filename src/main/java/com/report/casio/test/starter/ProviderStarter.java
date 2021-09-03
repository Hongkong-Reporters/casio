package com.report.casio.test.starter;

import com.report.casio.common.Constants;
import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.ServiceConfig;
import com.report.casio.config.parser.AnnotationBeanParser;
import com.report.casio.config.parser.ConfigParser;
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
        ConfigParser extension = ExtensionLoader.getExtensionLoader(ConfigParser.class).getDefaultExtension();
        extension.parse(Constants.DEFAULT_CONFIG_PATH);

        Set<ServiceConfig> serviceConfigs = AnnotationBeanParser.scanRegisterService("com.report.casio.test");
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
