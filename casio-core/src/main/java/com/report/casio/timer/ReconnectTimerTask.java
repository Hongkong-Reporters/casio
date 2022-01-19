package com.report.casio.timer;

import com.report.casio.remoting.transport.netty.TimerChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReconnectTimerTask extends AbstractTimerTask {
    private static final int TIMEOUT = 60 * 1000;

    @Override
    protected void doTask(TimerChannel channel) {
        long duration = Math.min(System.currentTimeMillis() - channel.getLastRead(), System.currentTimeMillis() - channel.getLastWrite());
        if (duration > TIMEOUT) {
            log.warn(channel.getChannel().localAddress() + " client connect timeout");
        }
    }
}
