package com.report.casio.common.utils;

public class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.isEmpty() || s.trim().isEmpty();
    }

}
