package com.report.casio.rpc.serialize;

import com.google.protobuf.AbstractMessage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hujiaofen
 * @since 29/4/2022
 */
public class ProtobufSerializer implements RpcSerializer{

    @Override
    public byte[] serialize(Object obj) throws IOException {
        if (obj instanceof AbstractMessage) {
            AbstractMessage message = (AbstractMessage) obj;
            return message.toByteArray();
        }
        throw new IOException("request message is not a proto message");
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        try {
            Method method = clazz.getDeclaredMethod("parseFrom", byte[].class);
            Object obj = method.invoke(null, bytes);
            return (T) obj;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IOException("response message is not a proto message");
        }
    }
}
