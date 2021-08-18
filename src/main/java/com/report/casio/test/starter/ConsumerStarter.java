package com.report.casio.test.starter;

import com.report.casio.remoting.transport.netty.client.NettyClient;
import com.report.casio.rpc.proxy.RpcClientProxy;
import com.report.casio.test.service.IDemoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerStarter implements CasioStarter {
    @Override
    public void start() {
        NettyClient client = new NettyClient();
        IDemoService service = new RpcClientProxy(client).getProxy(IDemoService.class);
        String hello = service.sayHello();
        log.info("result: " + hello);
    }

    public static void main(String[] args) {
        new ConsumerStarter().start();
    }

}
