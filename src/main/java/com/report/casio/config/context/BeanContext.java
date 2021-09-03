package com.report.casio.config.context;

import com.report.casio.config.ServiceConfig;
import com.report.casio.config.parser.AnnotationBeanParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 存储service的bean信息
public class BeanContext {

    private final Map<String, Object> beanMap = new ConcurrentHashMap<>();

    protected BeanContext() {
        String path = RpcContextFactory.getConfigContext().getProviderConfig().getServiceScanPackage();
        try {
            for (ServiceConfig serviceConfig : AnnotationBeanParser.scanRegisterService(path)) {
                beanMap.putIfAbsent(serviceConfig.getServiceName(), Class.forName(serviceConfig.getRef()).newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String serviceName) {
        return beanMap.get(serviceName);
    }

    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz.getName());
    }

}
