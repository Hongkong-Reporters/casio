package com.report.casio.test;

import com.report.casio.boostrap.Bootstraps;
import com.report.casio.rpc.proxy.RpcProxyUtil;
import com.report.casio.test.service.IDemoService;

/**
 * @author hujiaofen
 * @since 15/7/2022
 */
public class ApplicationTest {

    public static void main(String[] args) {
        Bootstraps bootstraps = new Bootstraps();
        bootstraps.start();
        bootstraps.registerService(IDemoService.class);

        try {
            // 启动等待时间
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        IDemoService service = RpcProxyUtil.getProxy(IDemoService.class);
        assert service != null;
        System.out.println(service.sayHello());
    }

}
