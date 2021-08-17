package com.report.casio.rpc.cluster.loadbalance;

public interface LoadBalance {

    <T> T select();

}
