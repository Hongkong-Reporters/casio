package com.report.casio.common.exception;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public class RemotingException extends Exception {
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    public RemotingException(Channel channel, String message) {
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, Throwable cause) {
        super(cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
