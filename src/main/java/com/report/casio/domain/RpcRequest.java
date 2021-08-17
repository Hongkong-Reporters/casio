package com.report.casio.domain;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class RpcRequest implements Serializable {
    private String requestId;
    private String serviceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String version;
}
