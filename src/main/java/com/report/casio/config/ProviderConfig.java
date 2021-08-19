package com.report.casio.config;

import lombok.SneakyThrows;

import java.net.InetAddress;

public class ProviderConfig {
    private final String address;
    private final int port;

    @SneakyThrows
    public ProviderConfig(int port) {
        this.address = InetAddress.getLocalHost().getHostAddress();
        this.port = port;
    }

    public String getHost() {
        return this.address + ":" + port;
    }

    public int getPort() {
        return port;
    }
}
