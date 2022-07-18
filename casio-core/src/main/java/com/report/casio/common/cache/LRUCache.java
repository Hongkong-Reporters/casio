package com.report.casio.common.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hujiaofen
 * @since 18/7/2022
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final Lock lock = new ReentrantLock();
    private final int cacheSize;

    public LRUCache(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > this.cacheSize;
    }

    @Override
    public V get(Object key) {
        this.lock.lock();
        try {
            return super.get(key);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        this.lock.lock();
        try {
            return super.remove(key);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        this.lock.lock();
        try {
            return super.put(key, value);
        } finally {
            this.lock.unlock();
        }
    }
}
