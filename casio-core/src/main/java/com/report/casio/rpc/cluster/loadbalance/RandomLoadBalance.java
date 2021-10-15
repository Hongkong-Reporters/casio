package com.report.casio.rpc.cluster.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {
    private final Random random = new Random();

    @Override
    public <T> T doSelect(List<T> addressList, String serviceName) {
        return addressList.get(random.nextInt(addressList.size()));
    }

}
