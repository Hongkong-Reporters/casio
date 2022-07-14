package com.report.casio.registry.zookeeper.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 消费者发现服务的cache，不需要每次调用都访问注册中心，没有修改的数据可以通过缓存获取，通过监听判断接口是否修改
public class ZkServiceDiscoveryCache {
    // key: servicePath, value: provider host addresses
    private static final Map<String, List<String>> serviceDiscoveryMap = new ConcurrentHashMap<>();

    private ZkServiceDiscoveryCache() {}

    public static void put(String servicePath, List<String> host) {
        serviceDiscoveryMap.putIfAbsent(servicePath, host);
    }

    public static List<String> get(String servicePath) {
        return serviceDiscoveryMap.get(servicePath);
    }

    public static void remove(String servicePath) {
        if (contains(servicePath)) {
            serviceDiscoveryMap.remove(servicePath);
        }
    }

    public static boolean contains(String servicePath) {
        return serviceDiscoveryMap.containsKey(servicePath);
    }
}
