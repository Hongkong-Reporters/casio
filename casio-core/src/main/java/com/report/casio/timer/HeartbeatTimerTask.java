package com.report.casio.timer;

import com.report.casio.domain.RpcMessage;
import com.report.casio.remoting.transport.netty.TimerChannel;
import com.report.casio.rpc.protocol.ProtocolConstants;

import java.nio.charset.StandardCharsets;

public class HeartbeatTimerTask extends AbstractTimerTask {
    private static final int DURATION = 15 * 1000;

    @Override
    protected void doTask(TimerChannel channel) {
        if (System.currentTimeMillis() - channel.getLastWrite() >= DURATION && System.currentTimeMillis() - channel.getLastRead() >= DURATION) {
            channel.setLastWrite();
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setType(ProtocolConstants.HEARTBEAT);
            rpcMessage.setContent("heartbeat".getBytes(StandardCharsets.UTF_8));
            channel.getChannel().writeAndFlush(rpcMessage);
        }
    }

}
