package com.report.casio.timer;

import com.report.casio.domain.RpcMessage;
import com.report.casio.remoting.transport.netty.client.cache.ChannelClient;
import com.report.casio.rpc.protocol.ProtocolConstants;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class WheelTimerJob {
    private final Map<Channel, Long> clientLastWriteMap = new ConcurrentHashMap<>();
    private final Map<Channel, Long> clientLastReadMap = new ConcurrentHashMap<>();
    private static final WheelTimerJob INSTANCE = new WheelTimerJob();
    private final WheelTimer wheelTimer = new WheelTimer(60, 1, TimeUnit.SECONDS);
    private final Lock clientLock = new ReentrantLock();

    private WheelTimerJob() {
        ChannelClient.getAllChannels().forEach(channel -> {
            clientLastWriteMap.put(channel, System.currentTimeMillis());
            clientLastReadMap.put(channel, System.currentTimeMillis());
        });
    }

    public static WheelTimerJob getInstance() {
        return INSTANCE;
    }

    public void execute() {
        wheelTimer.addTimeTask(() -> {
            Collection<Channel> clientChannels = ChannelClient.getAllChannels();
            try {
                clientLock.lock();
                long now = System.currentTimeMillis();
                clientChannels.forEach(channel -> {
                    if (now - clientLastReadMap.get(channel) >= 15 && now - clientLastWriteMap.get(channel) >= 15) {
                        RpcMessage rpcMessage = new RpcMessage();
                        rpcMessage.setType(ProtocolConstants.HEARTBEAT);
                        rpcMessage.setContent("success".getBytes(StandardCharsets.UTF_8));
                        channel.writeAndFlush(rpcMessage);
                    }
                });
            } finally {
                clientLock.unlock();
            }
        }, 15);
        wheelTimer.start();
    }

    public void setClientLastRead(Channel channel, long time) {
        try {
            clientLock.lock();
            this.clientLastReadMap.put(channel, time);
        } finally {
            clientLock.unlock();
        }
    }

    public void setClientLastWrite(Channel channel, long time) {
        try {
            clientLock.lock();
            this.clientLastWriteMap.put(channel, time);
        } finally {
            clientLock.unlock();
        }
    }

}
