package com.report.casio.domain;

import com.report.casio.rpc.protocol.ProtocolConstants;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcMessage implements Serializable {
    private int type;
    private byte version;
    private RpcRequest request;
    private RpcResponse response;
    private String msg;

    public RpcMessage() {}

    public RpcMessage(RpcRequest request) {
        this.type = ProtocolConstants.REQUEST_TYPE;
        this.request = request;
    }

    public RpcMessage(RpcResponse response) {
        this.type = ProtocolConstants.RESPONSE_TYPE;
        this.response = response;
    }

}
