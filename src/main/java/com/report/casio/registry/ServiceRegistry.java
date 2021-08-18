package com.report.casio.registry;

import com.report.casio.config.ServiceConfig;

public interface ServiceRegistry {
    void register(ServiceConfig serviceConfig) throws Exception;
}
