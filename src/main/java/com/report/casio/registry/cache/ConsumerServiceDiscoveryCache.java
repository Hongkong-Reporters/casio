package com.report.casio.registry.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 消费者发现服务的cache，不需要每次调用都访问注册中心，没有修改的数据可以通过缓存获取，通过监听判断接口是否修改
public class ConsumerServiceDiscoveryCache {
    // key: serviceName, value: provider host addresses
    private static final Map<String, List<String>> serviceDiscoveryMap = new ConcurrentHashMap<>();

    private ConsumerServiceDiscoveryCache() {}

    public static void put(String serviceName, List<String> host) {
        serviceDiscoveryMap.putIfAbsent(serviceName, host);
    }

    public static List<String> get(String serviceName) {
        return serviceDiscoveryMap.get(serviceName);
    }

    public static void remove(String serviceName) {
        if (contains(serviceName)) {
            serviceDiscoveryMap.remove(serviceName);
        }
    }

    public static boolean contains(String serviceName) {
        return serviceDiscoveryMap.containsKey(serviceName);
    }
}
