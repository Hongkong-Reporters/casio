package com.report.casio.config;

import com.report.casio.common.Constants;
import lombok.SneakyThrows;

import java.net.InetAddress;

public class ProviderConfig {
    private final String address;
    private final int port;
    private int timeout = Constants.DEFAULT_TIMEOUT;

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

    protected void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }
}
