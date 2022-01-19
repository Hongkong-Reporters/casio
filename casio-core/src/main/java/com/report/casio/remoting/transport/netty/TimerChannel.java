package com.report.casio.remoting.transport.netty;

import io.netty.channel.Channel;

public class TimerChannel {
    private final Channel channel;
    private long lastWrite;
    private long lastRead;

    public TimerChannel(Channel channel) {
        this.channel = channel;
        this.lastRead = System.currentTimeMillis();
        this.lastWrite = System.currentTimeMillis();
    }

    public Channel getChannel() {
        return channel;
    }

    public long getLastRead() {
        return lastRead;
    }

    public long getLastWrite() {
        return lastWrite;
    }

    public void setLastRead() {
        this.lastRead = System.currentTimeMillis();
    }

    public void setLastWrite() {
        this.lastWrite = System.currentTimeMillis();
    }
}