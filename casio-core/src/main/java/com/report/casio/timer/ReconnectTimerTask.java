package com.report.casio.timer;

import com.report.casio.remoting.transport.netty.TimerChannel;
import com.report.casio.remoting.transport.netty.client.NettyClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReconnectTimerTask extends AbstractTimerTask {
    private static final int TIMEOUT = 60 * 1000;
    private final NettyClient nettyClient;

    public ReconnectTimerTask(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    protected void doTask(TimerChannel channel) {
        long duration = Math.min(System.currentTimeMillis() - channel.getLastRead(), System.currentTimeMillis() - channel.getLastWrite());
        if (duration > TIMEOUT) {
            log.warn(channel.getChannel().localAddress() + " client connect timeout");
            nettyClient.reconnect(channel.getChannel());
        }
    }
}
