package com.report.casio.registry.cache.lru;

import com.report.casio.common.cache.LRUCache;
import com.report.casio.registry.cache.ServiceCache;

import java.util.List;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public class ServiceLruCache implements ServiceCache {
    private final LRUCache<String, List<String>> cache;

    public ServiceLruCache(int cacheSize) {
        this.cache = new LRUCache<>(cacheSize);
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
        cache.remove(key);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }
}
