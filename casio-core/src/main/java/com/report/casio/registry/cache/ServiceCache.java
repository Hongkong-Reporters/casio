package com.report.casio.registry.cache;

import java.util.List;

// 消费者发现服务的cache，不需要每次调用都访问注册中心，没有修改的数据可以通过缓存获取，通过监听判断接口是否修改
public interface ServiceCache {

    void put(String key, List<String> value);

    List<String> get(String key);

    void remove(String key);

    void clear();
}
