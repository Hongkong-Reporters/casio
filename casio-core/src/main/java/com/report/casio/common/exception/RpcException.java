package com.report.casio.common.exception;

public class RpcException extends RuntimeException {
    private final String requestId;

    public RpcException(String requestId, String message) {
        super(message);
        this.requestId = requestId;
    }

    public RpcException(String requestId, String message, Throwable cause) {
        super(message, cause);
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}
