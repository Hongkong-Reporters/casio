package com.report.casio.common;

public class Constants {
    private Constants() {}

    public static final String PROJECT = "casio";
    public static final String PROVIDER = "provider";
    public static final String CONSUMER = "consumer";
    public static final String REGISTRY = "registry";

    public static final String ADDRESS = "address";
    public static final String PORT = "port";
    public static final String TIMEOUT = "timeout";

    public static final int DEFAULT_TIMEOUT = 3000;
    public static final int DEFAULT_RETIES = 2;


    public static final int DEFAULT_PROVIDER_PORT = 9001;
    public static final String DEFAULT_REGISTRY_ADDRESS = "127.0.0.1";
    public static final int DEFAULT_REGISTRY_PORT = 2181;

    public static final String DEFAULT_CONFIG_PATH = "casio.yml";

}
