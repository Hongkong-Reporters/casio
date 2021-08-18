package com.report.casio.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcMessage implements Serializable {
    private byte type;
    private byte version;
    private byte[] content;
}
