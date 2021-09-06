package com.report.casio.config;

import com.report.casio.common.Constants;
import lombok.SneakyThrows;

import java.net.InetAddress;

public class ProviderConfig {
    private final String address;
    private final int port;
    private int timeout = Constants.DEFAULT_TIMEOUT;
    private String serviceScanPackage;

    @SneakyThrows
    public ProviderConfig(int port) {
        this.address = InetAddress.getLocalHost().getHostAddress();
        this.port = port;
    }

    public ProviderConfig(int port, int timeout) {
        this(port);
        this.timeout = timeout;
    }

    public String getHost() {
        return this.address + ":" + port;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getServiceScanPackage() {
        return serviceScanPackage;
    }

    public void setServiceScanPackage(String serviceScanPackage) {
        this.serviceScanPackage = serviceScanPackage;
    }
}
