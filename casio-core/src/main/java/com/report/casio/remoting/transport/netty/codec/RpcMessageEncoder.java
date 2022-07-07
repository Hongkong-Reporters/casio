package com.report.casio.remoting.transport.netty.codec;

import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.domain.RpcMessage;
import com.report.casio.rpc.protocol.ProtocolConstants;
import com.report.casio.rpc.serialize.RpcSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    @SneakyThrows
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) {
        byteBuf.writeByte(ProtocolConstants.MAGIC);
        byteBuf.writeByte(ProtocolConstants.VERSION);
        byteBuf.writeInt(rpcMessage.getType());
        RpcSerializer serializer = ExtensionLoader.getExtensionLoader(RpcSerializer.class).getDefaultExtension();
        byte[] content;
        if (rpcMessage.getType() == ProtocolConstants.REQUEST_TYPE) {
            content = serializer.serialize(rpcMessage.getRequest());
        } else if (rpcMessage.getType() == ProtocolConstants.RESPONSE_TYPE) {
            content = serializer.serialize(rpcMessage.getResponse());
        } else {
            content = serializer.serialize(rpcMessage.getMsg());
        }
        if (content != null) {
            byteBuf.writeInt(content.length);
            byteBuf.writeBytes(content);
        } else {
            log.warn("content is null, rpcMessage: " + rpcMessage);
        }
    }

}
