package com.report.casio.registry.cache;

import com.report.casio.common.annotation.SPI;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
@SPI("lru")
public interface ServiceCacheFactory {

    ServiceCache getCache();

    boolean createCache(int cacheSize);
}
