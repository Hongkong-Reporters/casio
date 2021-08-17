package com.report.casio.rpc.cluster.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public <T> T select(List<T> addressList, String serviceName) {
        if (addressList == null || addressList.isEmpty())
            return null;
        if (addressList.size() == 1)
            return addressList.get(0);
        return doSelect(addressList, serviceName);
    }

    public abstract <T> T doSelect(List<T> addressList, String serviceName);

    protected int getWeight() {
        return 0;
    }

}
