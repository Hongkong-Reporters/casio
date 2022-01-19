package com.report.casio.rpc.serialize;

import com.report.casio.common.annotation.SPI;

import java.io.IOException;

@SPI("byte")
public interface RpcSerializer {

    byte[] serialize(Object obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;

}
