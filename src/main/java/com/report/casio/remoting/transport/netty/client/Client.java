package com.report.casio.remoting.transport.netty.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public interface Client {

    Channel doConnect(InetSocketAddress inetSocketAddress);

    void doClose();

    Channel getChannel(InetSocketAddress inetSocketAddress);

}
