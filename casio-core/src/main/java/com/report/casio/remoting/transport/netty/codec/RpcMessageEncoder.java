package com.report.casio.remoting.transport.netty.codec;

import com.report.casio.domain.RpcMessage;
import com.report.casio.rpc.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) {
        byteBuf.writeByte(ProtocolConstants.MAGIC);
        byteBuf.writeByte(ProtocolConstants.VERSION);
        byteBuf.writeInt(rpcMessage.getType());
        byteBuf.writeInt(rpcMessage.getContent().length);
        byteBuf.writeBytes(rpcMessage.getContent());
    }

}
