package com.report.casio.timer;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WheelTimerJob {
    private static final WheelTimerJob INSTANCE = new WheelTimerJob();
    private static final WheelTimer wheelTimer = new WheelTimer(60, 1, TimeUnit.SECONDS);

    private WheelTimerJob() {
    }

    public static WheelTimerJob getInstance() {
        return INSTANCE;
    }

    public void execute() {
        wheelTimer.addTimeTask(new HeartbeatTimerTask(), 1);
        wheelTimer.addTimeTask(new ReconnectTimerTask(), 1);
        wheelTimer.start();
    }

}
