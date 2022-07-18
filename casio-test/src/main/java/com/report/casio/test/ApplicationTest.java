package com.report.casio.test;

import com.report.casio.boostrap.Bootstraps;
import com.report.casio.embed.EmbeddedZookeeper;
import com.report.casio.rpc.proxy.RpcProxyUtil;
import com.report.casio.test.service.IDemoService;

/**
 * @author hujiaofen
 * @since 15/7/2022
 */
public class ApplicationTest {

    public static void main(String[] args) {
        new EmbeddedZookeeper(2181, false, "C:\\Ceilzcx\\project\\data\\zookeeper").start();
        try {
            // zk启动等待时间
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        Bootstraps bootstraps = new Bootstraps();
        bootstraps.start();
        bootstraps.registerService(IDemoService.class);

        try {
            // casio启动等待时间
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        IDemoService service = RpcProxyUtil.getProxy(IDemoService.class);
        assert service != null;
        for (int i = 0; i < 10; i++) {
            System.out.println(service.sayHello());
        }
    }

}
