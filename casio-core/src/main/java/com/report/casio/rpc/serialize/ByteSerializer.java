package com.report.casio.rpc.serialize;

import java.io.*;

public class ByteSerializer implements RpcSerializer {

    @Override
    public byte[] serialize(Object obj) throws IOException {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream sOut = new ObjectOutputStream(out)
        ) {
            sOut.writeObject(obj);
            sOut.flush();
            return out.toByteArray();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        try (
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                ObjectInputStream sIn = new ObjectInputStream(in)
        ) {
            return clazz.cast(sIn.readObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
