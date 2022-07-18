package com.report.casio.common.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hujiaofen
 * @since 18/7/2022
 * LRU：least recently used（最近最少使用）
 * 实现方案：通过链表实现，最新的访问的数据放在最右边（已经存在的数据也要挪到最右边）
 *         当数据达到最大缓存，删除最左边的数据
 * LinkedHashMap的removeEldestEntry已经实现了LRU
 * 缺点：一次性或者周期性的数据过多会占用cache空间，导致cache都是无用的数据
 * todo：可以通过LRU-K提高缓存命中率
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
