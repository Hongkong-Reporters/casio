package com.report.casio.timer;

import com.report.casio.remoting.transport.netty.client.NettyClient;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WheelTimerJob {
    private final WheelTimer wheelTimer = new WheelTimer(60, 1, TimeUnit.SECONDS);
    private final NettyClient nettyClient;

    public WheelTimerJob(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }


    public void execute() {
        wheelTimer.addTimeTask(new HeartbeatTimerTask(), 1);
        wheelTimer.addTimeTask(new ReconnectTimerTask(nettyClient), 1);
        wheelTimer.start();
    }

    public void close() {
        wheelTimer.close();
    }

}
