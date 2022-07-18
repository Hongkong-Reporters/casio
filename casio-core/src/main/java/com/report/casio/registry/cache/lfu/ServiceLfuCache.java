package com.report.casio.registry.cache.lfu;

import com.report.casio.common.cache.LFUCache;
import com.report.casio.registry.cache.ServiceCache;

import java.util.List;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public class ServiceLfuCache implements ServiceCache {
    private final LFUCache<String, List<String>> cache;

    public ServiceLfuCache(int cacheSize) {
        this.cache = new LFUCache<>(cacheSize);
    }

    @Override
    public void put(String key, List<String> value) {
        this.cache.put(key, value);
    }

    @Override
    public List<String> get(String key) {
        return this.cache.get(key);
    }

    @Override
    public void remove(String key) {
        this.cache.remove(key);
    }


    @Override
    public void clear() {
        this.cache.clear();
    }
}
