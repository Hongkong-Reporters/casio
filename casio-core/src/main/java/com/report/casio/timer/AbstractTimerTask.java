package com.report.casio.timer;

import com.report.casio.remoting.transport.netty.TimerChannel;
import com.report.casio.remoting.transport.netty.client.cache.ChannelClient;

import java.util.Collection;

public abstract class AbstractTimerTask implements TimerTask {

    @Override
    public void start() {
        for (TimerChannel channel : getChannels()) {
            doTask(channel);
        }
    }

    public Collection<TimerChannel> getChannels() {
        return ChannelClient.getAllChannels();
    }

    protected abstract void doTask(TimerChannel channel);

}
