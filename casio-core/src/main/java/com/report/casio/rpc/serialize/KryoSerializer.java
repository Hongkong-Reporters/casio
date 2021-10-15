package com.report.casio.rpc.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.report.casio.domain.RpcMessage;
import com.report.casio.domain.RpcRequest;
import com.report.casio.domain.RpcResponse;

public class KryoSerializer implements RpcSerializer {
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        // 默认支持循环引用
        Kryo kryo = new Kryo();
        // 关闭序列化注册，防止分布式环境下ID不一致
        kryo.setRegistrationRequired(false);
        // 防止CNF
        kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
        kryo.register(RpcMessage.class);
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (Output output = new Output(1024, -1)){
            kryoThreadLocal.get().writeClassAndObject(output, obj);
            output.flush();
            kryoThreadLocal.remove();
            return output.getBuffer();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (Input input = new Input(bytes)){
            T result = kryoThreadLocal.get().readObject(input, clazz);
            kryoThreadLocal.remove();
            return result;
        }
    }


}
