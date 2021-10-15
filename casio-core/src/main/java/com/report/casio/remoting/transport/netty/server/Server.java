package com.report.casio.remoting.transport.netty.server;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public interface Server {

    void doOpen();

    void doClose();

    Channel getChannel(InetSocketAddress inetSocketAddress);

}
