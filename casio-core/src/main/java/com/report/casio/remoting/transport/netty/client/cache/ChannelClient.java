package com.report.casio.remoting.transport.netty.client.cache;

import com.report.casio.remoting.transport.netty.TimerChannel;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 保存Channel信息，用于客户端数据发送
public class ChannelClient {
    private ChannelClient() {}

    private static final Map<InetSocketAddress, TimerChannel> channelMap = new ConcurrentHashMap<>();

    public static TimerChannel get(InetSocketAddress inetSocketAddress) {
        return channelMap.get(inetSocketAddress);
    }

    public static void put(InetSocketAddress inetSocketAddress, TimerChannel channel) {
        channelMap.putIfAbsent(inetSocketAddress, channel);
    }

    public static void remove(InetSocketAddress inetSocketAddress) {
        channelMap.remove(inetSocketAddress);
    }

    public static Collection<TimerChannel> getAllChannels() {
        return channelMap.values();
    }
}
