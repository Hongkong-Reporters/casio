package com.report.casio.config;

import com.report.casio.registry.ServiceDiscovery;
import com.report.casio.rpc.cluster.loadbalance.LoadBalance;

public class RpcContext {
      private LoadBalance defaultLoadBalance;
      private ServiceDiscovery defaultServiceDiscovery;

      public LoadBalance getDefaultLoadBalance() {
            return defaultLoadBalance;
      }

      public ServiceDiscovery getDefaultServiceDiscovery() {
            return defaultServiceDiscovery;
      }
}
