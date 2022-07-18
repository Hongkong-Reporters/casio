package com.report.casio.registry.cache.lfu;

import com.report.casio.registry.cache.AbstractServiceCacheFactory;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public class ServiceLfuCacheFactory extends AbstractServiceCacheFactory {

    @Override
    public boolean createCache(int cacheSize) {
        if (serviceCache == null) {
            serviceCache = new ServiceLfuCache(cacheSize);
            return true;
        }
        return false;
    }
}
