package com.report.casio.registry.cache.lru;

import com.report.casio.registry.cache.AbstractServiceCacheFactory;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public class ServiceLruCacheFactory extends AbstractServiceCacheFactory {

    @Override
    public boolean createCache(int cacheSize) {
        if (serviceCache == null) {
            serviceCache = new ServiceLruCache(cacheSize);
            return true;
        }
        return false;
    }
}
