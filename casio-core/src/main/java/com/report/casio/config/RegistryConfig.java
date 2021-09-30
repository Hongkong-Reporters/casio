package com.report.casio.config;

import lombok.Data;

@Data
public class RegistryConfig {
    private String address;
    private Integer port;
    private String username;
    private String password;
    private String version;
    private String protocol;

    public String getHost() {
        return address + ":" + port;
    }

}
