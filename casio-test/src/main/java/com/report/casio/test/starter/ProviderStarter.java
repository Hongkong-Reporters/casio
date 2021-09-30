package com.report.casio.test.starter;

import com.report.casio.config.CasioConfigInitHandler;
import com.report.casio.remoting.transport.netty.server.NettyServer;
import com.report.casio.remoting.transport.netty.server.Server;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProviderStarter implements CasioStarter {

    @SneakyThrows
    @Override
    public void start() {
        new CasioConfigInitHandler().init();

        log.info("service registry success");
        Server server = new NettyServer();
        server.doOpen();
        log.info("server open success");
    }

    public static void main(String[] args) {
        new ProviderStarter().start();
    }

}