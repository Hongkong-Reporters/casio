package com.report.casio.registry;

import com.report.casio.common.annotation.SPI;
import com.report.casio.config.ServiceConfig;

@SPI("zk")
public interface ServiceRegistry {
    void register(ServiceConfig serviceConfig) throws Exception;
}
