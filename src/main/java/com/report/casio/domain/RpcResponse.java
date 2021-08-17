package com.report.casio.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RpcResponse implements Serializable {
    private Object result;
    private RpcStatus status;
    private Throwable exception;
}
