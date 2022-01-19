package com.report.casio.remoting.transport.netty.codec;

import com.report.casio.domain.RpcMessage;
import com.report.casio.rpc.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        this(ProtocolConstants.MAX_FRAME_LENGTH, 6, 4,
                -ProtocolConstants.MIN_LENGTH,0);
    }

    // maxFrameLength: 控制最大帧长度
    // lengthFieldOffset: 长度字段的具体下标，一般为数据长度
    // lengthFieldLength: 长度字段占用字节数
    // lengthFieldOffset: 长度调节器，如果长度字段表示总长度，设置为头部长度，否则为负数
    // initialBytesToStrip: 忽略的字节数，如果只关心数据部分，可以设置头部长度
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) {
        if (in.readableBytes() < ProtocolConstants.MIN_LENGTH) {
            log.error("数据解码有误，长度小于" + ProtocolConstants.MIN_LENGTH);
            return null;
        }
        int i;
        while (true) {
            i = in.readerIndex();
            in.markReaderIndex();
            if (in.readByte() == ProtocolConstants.MAGIC) {
                break;
            }
            in.resetReaderIndex();
            in.readByte();
            if (in.readableBytes() < ProtocolConstants.MIN_LENGTH) return null;
        }
        byte version  = in.readByte();
        int type = in.readInt();
        int length = in.readInt();
        // 判断包是否完整，否则还原指针位置
        if (in.readableBytes() < length) {
            in.readerIndex(i);
            return null;
        }
        byte[] data = new byte[length];
        in.readBytes(data);
        RpcMessage message = new RpcMessage();
        message.setVersion(version);
        message.setType(type);
        message.setContent(data);
        return message;
    }
}
