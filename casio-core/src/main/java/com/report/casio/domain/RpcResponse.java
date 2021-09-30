package com.report.casio.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RpcResponse implements Serializable {
    private String requestId;
    private Object result;
    private RpcStatus status;
    private Throwable exception;
}
