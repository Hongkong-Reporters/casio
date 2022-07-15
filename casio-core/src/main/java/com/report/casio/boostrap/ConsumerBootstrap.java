package com.report.casio.boostrap;

import com.report.casio.config.ConsumerConfig;
import com.report.casio.remoting.transport.netty.client.NettyClient;

/**
 * @author hujiaofen
 * @since 15/7/2022
 */
public class ConsumerBootstrap implements Bootstrap {
    private final ConsumerConfig consumerConfig;
    private final NettyClient nettyClient;

    public ConsumerBootstrap(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
        this.nettyClient = new NettyClient();
    }

    @Override
    public void start() {
        if (consumerConfig.isCheck()) {
            // 检查provider服务
        }
    }

    @Override
    public void close() {
        this.nettyClient.doClose();
    }

    public NettyClient getNettyClient() {
        return nettyClient;
    }
}
