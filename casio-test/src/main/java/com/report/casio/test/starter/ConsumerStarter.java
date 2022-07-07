package com.report.casio.test.starter;

import com.report.casio.config.CasioConfigInitHandler;
import com.report.casio.remoting.transport.netty.client.NettyClient;
import com.report.casio.rpc.proxy.RpcClientProxy;
import com.report.casio.test.service.IDemoService;
import com.report.casio.timer.WheelTimerJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerStarter implements CasioStarter {

    @Override
    @SneakyThrows
    public void start() {
        new CasioConfigInitHandler().init();

        NettyClient client = NettyClient.getInstance();
        IDemoService service = new RpcClientProxy(client).getProxy(IDemoService.class);
        WheelTimerJob.getInstance().execute();

        for (int i = 0; i < 10; i++) {
            service.sayHello();
            Thread.sleep(10000);
        }

    }

    public static void main(String[] args) {
        new ConsumerStarter().start();
    }

}
