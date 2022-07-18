package com.report.casio.registry.cache;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public abstract class AbstractServiceCacheFactory implements ServiceCacheFactory {
    protected ServiceCache serviceCache;

    @Override
    public ServiceCache getCache() {
        return serviceCache;
    }
}
