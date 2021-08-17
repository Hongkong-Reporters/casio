package com.report.casio.common.exception;

public class RpcException extends RuntimeException {
    private int code;

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
