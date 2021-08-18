package com.report.casio.common.utils;

import com.report.casio.common.Constants;

public class StringUtils {
    private static final String ZK_SEPARATOR = "/";

    private StringUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.isEmpty() || s.trim().isEmpty();
    }

    public static String generateProviderPath(String serviceName) {
        return Constants.PROJECT + ZK_SEPARATOR +
                serviceName +
                Constants.PROVIDER + ZK_SEPARATOR;
    }

}
