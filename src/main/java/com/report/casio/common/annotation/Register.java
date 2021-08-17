package com.report.casio.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
// 服务注册注解
public @interface Register {
    String version() default "";
}