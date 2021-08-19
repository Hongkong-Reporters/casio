package com.report.casio.rpc.cluster.loadbalance;

import com.report.casio.common.annotation.SPI;

import java.util.List;

@SPI("round")
public interface LoadBalance {

    <T> T select(List<T> addressList, String serviceName);

}
