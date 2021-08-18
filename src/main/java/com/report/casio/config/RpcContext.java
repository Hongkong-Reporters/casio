package com.report.casio.config;

import com.report.casio.registry.ServiceDiscovery;
import com.report.casio.registry.zookeeper.ZkServiceDiscovery;
import com.report.casio.rpc.cluster.loadbalance.LoadBalance;
import com.report.casio.rpc.cluster.loadbalance.RoundLoadBalance;

public class RpcContext {
      private LoadBalance defaultLoadBalance = new RoundLoadBalance();
      private ServiceDiscovery defaultServiceDiscovery = new ZkServiceDiscovery();

      public LoadBalance getDefaultLoadBalance() {
            return defaultLoadBalance;
      }

      public ServiceDiscovery getDefaultServiceDiscovery() {
            return defaultServiceDiscovery;
      }
}
