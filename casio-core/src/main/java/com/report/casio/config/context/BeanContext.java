package com.report.casio.config.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContext {
    private final Map<String, Object> beanMap = new ConcurrentHashMap<>();

    public void registerService(String key, Object obj) {
        beanMap.putIfAbsent(key, obj);
    }

    public Object getBean(String serviceName) {
        return beanMap.get(serviceName);
    }

}
