package com.report.casio;

import com.report.casio.common.utils.AnnScanUtils;
import com.report.casio.config.ServiceConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Set;

@Slf4j
public class ScanTest {

    @SneakyThrows
    @Test
    public void scanTest() {
        Set<ServiceConfig> serviceConfigs = AnnScanUtils.scanRegisterService("com.report.casio");
        for (ServiceConfig serviceConfig : serviceConfigs) {
            log.info("serviceName: " + serviceConfig.getServiceName());
        }
    }

}
