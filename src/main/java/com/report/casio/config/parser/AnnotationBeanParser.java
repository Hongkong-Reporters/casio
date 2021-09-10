package com.report.casio.config.parser;

import com.report.casio.common.annotation.Register;
import com.report.casio.common.utils.StringUtils;
import com.report.casio.config.ServiceConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class AnnotationBeanParser {
    private static final String CLASS_SUFFIX = ".class";

    private AnnotationBeanParser() {
    }

    public static Set<ServiceConfig> scanRegisterService(String packageName) throws ClassNotFoundException {
        Set<ServiceConfig> res = new HashSet<>();

        Set<Class<?>> classes = scan(packageName);
        for (Class<?> clazz : classes) {
            Register register = clazz.getAnnotation(Register.class);
            if (register != null) {
                ServiceConfig serviceConfig = new ServiceConfig();
                serviceConfig.setVersion(register.version());
                serviceConfig.setRef(clazz.getName());
                if (register.interfaceClass().equals(void.class)) {
                    serviceConfig.setServiceName(clazz.getInterfaces()[0].getName());
                } else {
                    serviceConfig.setServiceName(register.interfaceClass().getName());
                }
                res.add(serviceConfig);
            }
        }

        return res;
    }

    /**
     * 查询指定包下指定注解修饰的类，可以参考Spring的PackageScan
     *
     * @param packageName     包名
     * @param annotationClass 注解Class
     */
    public static Set<Class<?>> scan(String packageName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
        Set<Class<?>> res = new HashSet<>();

        Set<Class<?>> classes = scan(packageName);
        for (Class<?> clazz : classes) {
            if (clazz.getAnnotation(annotationClass) != null) {
                res.add(clazz);
            }
        }

        return res;
    }

    public static Set<Class<?>> scan(String packageName) throws ClassNotFoundException {
        Set<Class<?>> res = new HashSet<>();
        if (StringUtils.isBlank(packageName)) {
            return res;
        }

        String packagePath = packageName.replace(".", "/");
        URL resource = AnnotationBeanParser.class.getClassLoader().getResource(packagePath);
        if (resource == null) {
            log.error("资源不存在：" + packageName);
            return res;
        }
        File rootDir = new File(resource.getFile());
        if (!rootDir.exists()) {
            log.error("资源文件夹不存在：" + packageName);
        }
        Set<String> classPaths = new HashSet<>();
        getAllClassPath(rootDir, classPaths);
        for (String className : classPaths) {
            res.add(Class.forName(getClassName(className, packageName)));
        }

        return res;
    }

    // 递归返回所有 .class 结尾的文件名
    private static void getAllClassPath(File file, Set<String> classPaths) {
        if (file.isDirectory()) {
            for (File childFile : Objects.requireNonNull(file.listFiles())) {
                getAllClassPath(childFile, classPaths);
            }
        } else {
            if (isClassPath(file.getAbsolutePath())) {
                classPaths.add(file.getAbsolutePath());
            }
        }
    }

    // 路径转类名
    private static String getClassName(String path, String packageName) {
        String res = path.replace(File.separator, ".");
        int left = res.indexOf(packageName);
        int right = res.length() - CLASS_SUFFIX.length();
        return res.substring(left, right);
    }

    private static boolean isClassPath(String path) {
        return !StringUtils.isBlank(path) && path.contains(CLASS_SUFFIX);
    }
}
