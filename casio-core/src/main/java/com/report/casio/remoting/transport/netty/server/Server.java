package com.report.casio.remoting.transport.netty.server;

import io.netty.channel.Channel;

public interface Server {

    void doOpen();

    void doClose();

    Channel getChannel();

}
