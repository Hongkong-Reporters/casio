package com.report.casio.config;

import com.report.casio.common.Constants;
import lombok.SneakyThrows;

import java.net.InetAddress;

public class ProviderConfig {
    private final String address;
    private final int port;
    private int timeout = Constants.DEFAULT_TIMEOUT;
    // 服务扫描base包，添加到BeanContext中，目前仅供测试，还没想好如何注入这个属性
    private String serviceScanPackage = "com.report.casio.test";

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
}
