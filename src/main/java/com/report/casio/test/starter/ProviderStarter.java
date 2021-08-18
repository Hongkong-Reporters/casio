package com.report.casio.test.starter;

import com.report.casio.common.utils.AnnScanUtils;
import com.report.casio.config.RpcContext;
import com.report.casio.config.RpcContextFactory;
import com.report.casio.config.ServiceConfig;
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
        Set<ServiceConfig> serviceConfigs = AnnScanUtils.scanRegisterService("com.report.casio");
        RpcContext context = RpcContextFactory.getRpcContext();
        for (ServiceConfig serviceConfig : serviceConfigs) {
            context.getDefaultServiceRegistry().register(serviceConfig);
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
