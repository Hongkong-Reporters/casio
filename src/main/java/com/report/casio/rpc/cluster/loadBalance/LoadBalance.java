package com.report.casio.rpc.cluster.loadbalance;

import java.util.List;

public interface LoadBalance {

    <T> T select(List<T> addressList, String serviceName);

}
