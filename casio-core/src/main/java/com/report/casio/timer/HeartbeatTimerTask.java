package com.report.casio.timer;

import com.report.casio.domain.RpcMessage;
import com.report.casio.remoting.transport.netty.TimerChannel;
import com.report.casio.rpc.protocol.ProtocolConstants;

public class HeartbeatTimerTask extends AbstractTimerTask {
    private static final int DURATION = 15 * 1000;

    @Override
    protected void doTask(TimerChannel channel) {
        if (System.currentTimeMillis() - channel.getLastWrite() >= DURATION && System.currentTimeMillis() - channel.getLastRead() >= DURATION) {
            channel.setLastWrite();
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setType(ProtocolConstants.HEARTBEAT);
            rpcMessage.setMsg("heartbeat");
            channel.getChannel().writeAndFlush(rpcMessage);
        }
    }

}
