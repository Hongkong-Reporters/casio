package com.report.casio.common.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hujiaofen
 * @since 18/7/2022
 * LFU：Least Frequently Used 最近使用频次最低
 * 架构：索引表 --- map           存储 <key, cache node> cache node是双向链表
 *      Frequency表 --- freqTable           ↓
 *      |0|list01| <---> cache node <---> cache node <---> cache node
 *           ↓
 *      |1|list02| <---> cache node <---> cache node <---> cache node
 *           ↓
 *      |2|list03| <---> cache node <---> cache node <---> cache node
 * 实现：
 * 每次getKey，删除当前list的cacheNode，在下一个list最末尾添加cacheNode
 * 每次put，在list01最末尾添加；判断是否超过size，超过删除第一个Node
 */
public class LFUCache<K, V> {
    private final Lock lock = new ReentrantLock();
    private final int cacheSize;
    private final Map<K, CacheNode<K, V>> map;
    private final CacheDeque<K, V>[] freqTable;

    public LFUCache(int cacheSize) {
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("illegal invalid cache size: " + cacheSize);
        }
        this.cacheSize = cacheSize;
        this.map = new HashMap<>(cacheSize);
        this.freqTable = new CacheDeque[cacheSize];
        for (int i = 0; i < cacheSize; i++) {
            this.freqTable[i] = new CacheDeque<>();
        }
        for (int i = 0; i < cacheSize - 1; i++) {
            this.freqTable[i].nextDeque = this.freqTable[i + 1];
        }
        // 频率达到cacheSize，不往上递增
        this.freqTable[cacheSize - 1].nextDeque = this.freqTable[cacheSize - 1];
    }

    public void put(K key, V value) {
        this.lock.lock();
        try {
            CacheNode<K, V> node = map.get(key);
            if (node != null) {
                node.value = value;
                CacheNode.removeNode(node);
            } else {
                node = new CacheNode<>(key, value);
                if (this.map.size() + 1 > this.cacheSize) {
                    for (int i = 0; i < this.cacheSize; i++) {
                        if (!this.freqTable[i].isEmpty()) {
                            CacheNode<K, V> delNode = this.freqTable[i].removeFirst();
                            if (delNode != null) {
                                this.map.remove(delNode.key);
                                break;
                            }
                        }
                    }
                }
                this.map.put(key, node);
                this.freqTable[0].addLastNode(node);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public V get(K key) {
        this.lock.lock();
        try {
            CacheNode<K, V> node = this.map.get(key);
            if (node == null) {
                return null;
            }
            CacheNode.removeNode(node);
            node.ownerDeque.nextDeque.addLastNode(node);
            return node.value;
        } finally {
            this.lock.unlock();
        }
    }

    public void remove(K key) {
        this.lock.lock();
        try {
            if (this.map.containsKey(key)) {
                CacheNode<K, V> node = this.map.get(key);
                CacheNode.removeNode(node);
                this.map.remove(node.key);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void clear() {
        this.map.clear();
        for (int i = 0; i < cacheSize; i++) {
            this.freqTable[i] = new CacheDeque<>();
        }
        for (int i = 0; i < cacheSize - 1; i++) {
            this.freqTable[i].nextDeque = this.freqTable[i + 1];
        }
        this.freqTable[cacheSize - 1].nextDeque = this.freqTable[cacheSize - 1];
    }

    static class CacheNode<K, V> {
        private CacheNode<K, V> prevNode;
        private CacheNode<K, V> nextNode;
        private K key;
        private V value;
        private CacheDeque<K, V> ownerDeque;

        public CacheNode() {
        }

        public CacheNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public static <K, V> void removeNode(CacheNode<K, V> cacheNode) {
            if (cacheNode != null) {
                CacheNode<K, V> prevNode = cacheNode.prevNode;
                if (cacheNode.prevNode != null) {
                    cacheNode.prevNode.nextNode = cacheNode.nextNode;
                }
                if (cacheNode.nextNode != null) {
                    cacheNode.nextNode.prevNode = prevNode;
                }
                cacheNode.prevNode = null;
                cacheNode.nextNode = null;
            }
        }
    }

    static class CacheDeque<K, V> {
        private final CacheNode<K, V> firstNode;
        private CacheNode<K, V> lastNode;
        private CacheDeque<K, V> nextDeque;

        public CacheDeque() {
            this.firstNode = new CacheNode<>();
            this.lastNode = this.firstNode;
        }

        public CacheNode<K, V> removeFirst() {
            CacheNode<K, V> prevFirstNode = this.firstNode.nextNode;
            if (prevFirstNode != null) {
                this.firstNode.nextNode = prevFirstNode.nextNode;
                prevFirstNode.prevNode = null;
                prevFirstNode.nextNode = null;
            }
            return prevFirstNode;
        }

        public void addLastNode(CacheNode<K, V> node) {
            CacheNode<K, V> prevLastNode = this.lastNode;
            if (prevLastNode != null) {
                node.prevNode = prevLastNode;
                node.nextNode = prevLastNode.nextNode;
                prevLastNode.nextNode = node;
            }
            this.lastNode = node;
            node.ownerDeque = this;
        }

        public boolean isEmpty() {
            return this.firstNode == this.lastNode;
        }
    }
}