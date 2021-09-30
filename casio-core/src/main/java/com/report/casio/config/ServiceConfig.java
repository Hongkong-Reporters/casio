package com.report.casio.config;

import lombok.Data;

import java.util.Set;

// 服务注册的Service
@Data
public class ServiceConfig {
    // 路径简单使用String代表：ip + port, 可以参考dubbo使用URL类表示
    private Set<String> hosts;
    private String serviceName;
    private String ref;
    private String version;
}
