package com.report.casio.rpc.protocol;

public class ProtocolConstants {
    private ProtocolConstants() {}

    public static final byte MAGIC = 0x27;
    public static final byte VERSION = 0x01;

    public static final byte REQUEST_TYPE = 0x00;
    public static final byte RESPONSE_TYPE = 0x01;

    // magic(1) + version (1) + type (1) + length (4) 共7个字节
    public static final int MIN_LENGTH = 7;

    public static final int MAX_FRAME_LENGTH = 1024 * 1024;

}
