package com.report.casio.domain;

import com.report.casio.common.utils.ByteUtils;
import com.report.casio.rpc.protocol.ProtocolConstants;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

@Data
public class RpcMessage implements Serializable {
    private byte type;
    private byte version;
    private byte[] content;

    public RpcMessage() {}

    public RpcMessage(RpcRequest request) throws IOException {
        this.type = ProtocolConstants.REQUEST_TYPE;
        this.content = ByteUtils.objectToBytes(request);
    }

    public RpcMessage(RpcResponse response) throws IOException {
        this.type = ProtocolConstants.RESPONSE_TYPE;
        this.content = ByteUtils.objectToBytes(response);
    }

}
