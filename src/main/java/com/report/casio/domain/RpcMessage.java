package com.report.casio.domain;

import lombok.Data;

@Data
public class RpcMessage {
    private byte type;
    private byte version;
    private byte[] content;
}
