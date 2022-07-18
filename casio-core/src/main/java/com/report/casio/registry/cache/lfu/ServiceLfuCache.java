package com.report.casio.registry.cache.lfu;

import com.report.casio.registry.cache.ServiceCache;

import java.util.List;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public class ServiceLfuCache implements ServiceCache {

    public ServiceLfuCache(int cacheSize) {
    }

    @Override
    public void put(String key, List<String> value) {

    }

    @Override
    public List<String> get(String key) {
        return null;
    }

    @Override
    public void remove(String key) {

    }


    @Override
    public void clear() {

    }
}
