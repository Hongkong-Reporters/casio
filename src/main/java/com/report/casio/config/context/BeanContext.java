package com.report.casio.config.context;

import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.ServiceConfig;
import com.report.casio.config.parser.AnnotationBeanParser;
import com.report.casio.registry.ServiceRegistry;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContext {
    private final Map<String, Object> beanMap = new ConcurrentHashMap<>();

    protected BeanContext() {
    }

    @SneakyThrows
    public void init() {
        ServiceRegistry serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getDefaultExtension();
        for (ServiceConfig serviceConfig : AnnotationBeanParser.scanRegisterService("com.report.casio.test")) {
            beanMap.putIfAbsent(serviceConfig.getServiceName(), Class.forName(serviceConfig.getRef()).newInstance());
            serviceRegistry.register(serviceConfig);
        }
    }

    public Object getBean(String serviceName) {
        return beanMap.get(serviceName);
    }

}
