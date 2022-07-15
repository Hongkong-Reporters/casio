package com.report.casio.config;

import com.report.casio.common.annotation.Register;

import java.util.Set;

// 服务注册的Service
public class ServiceConfig {
    // 路径简单使用String代表：ip + port, 可以参考dubbo使用URL类表示
    private final Set<String> hosts;
    private final String serviceName;
    private final String ref;
    private final String version;

    private ServiceConfig(Builder builder) {
        this.hosts = builder.hosts;
        this.serviceName = builder.serviceName;
        this.ref = builder.ref;
        this.version = builder.version;
    }

    public static class Builder {
        private Set<String> hosts;
        private String serviceName;
        private String ref;
        private String version;

        public Builder hosts(Set<String> hosts) {
            this.hosts = hosts;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder serviceName(Class<?> serviceClass) {
            Register register = serviceClass.getAnnotation(Register.class);
            String name;
            if (register != null) {
                if (register.interfaceClass().equals(void.class)) {
                    name = serviceClass.getInterfaces()[0].getName();
                } else {
                    name = register.interfaceClass().getName();
                }
            } else {
                name = serviceClass.getName();
            }
            return serviceName(name);
        }

        public Builder ref(String ref) {
            this.ref = ref;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public ServiceConfig build() {
            return new ServiceConfig(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public String getVersion() {
        return version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getRef() {
        return ref;
    }

    public Set<String> getHosts() {
        return hosts;
    }
}