package com.report.casio.common.exception;

public class RpcException extends RuntimeException {
    private String requestId;

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(String msg) {
        super(msg);
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}
