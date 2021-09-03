package com.report.casio.test.starter;

import com.report.casio.common.Constants;
import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.parser.ConfigParser;
import com.report.casio.remoting.transport.netty.client.NettyClient;
import com.report.casio.rpc.proxy.RpcClientProxy;
import com.report.casio.test.service.IDemoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerStarter implements CasioStarter {

    @Override
    @SneakyThrows
    public void start() {
        ConfigParser extension = ExtensionLoader.getExtensionLoader(ConfigParser.class).getDefaultExtension();
        extension.parse(Constants.DEFAULT_CONFIG_PATH);

        NettyClient client = new NettyClient();
        IDemoService service = new RpcClientProxy(client).getProxy(IDemoService.class);

        for (int i = 0; i < 100; i++) {
            service.sayHello();
        }
    }

    public static void main(String[] args) {
        new ConsumerStarter().start();
    }

}
