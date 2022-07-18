package com.report.casio.common.utils;

/**
 * @author hujiaofen
 * @since 15/7/2022
 */
public class SystemUtil {

    private SystemUtil() {}

    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        return osName.startsWith("linux");
    }
}
