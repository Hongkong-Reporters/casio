package com.report.casio.rpc.cluster.loadbalance;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundLoadBalance extends AbstractLoadBalance {
    private final ConcurrentMap<String, AtomicInteger> sequences = new ConcurrentHashMap<>();

    @Override
    // 使用CAS自旋锁
    public <T> T doSelect(List<T> addressList, String serviceName) {
        if (!sequences.containsKey(serviceName)) {
            // 使用putIfAbsent，而不是put，类似CAS一样，防止多线程出现问题
            sequences.putIfAbsent(serviceName, new AtomicInteger(0));
        }
        // 使用取模的形式，i的值会越来越大，等待达到Integer.MAX_VALUE是否会出问题？不过一般做不到这么大的数值
        AtomicInteger atomicInteger = sequences.get(serviceName);
        int i = atomicInteger.incrementAndGet();
        return addressList.get(i % addressList.size());
    }

}
