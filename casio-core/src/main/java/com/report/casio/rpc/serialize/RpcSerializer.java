package com.report.casio.rpc.serialize;

public interface RpcSerializer {

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
