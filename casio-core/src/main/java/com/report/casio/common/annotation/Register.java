package com.report.casio.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
// 服务注册注解
public @interface Register {
    // 默认可以不提供，如果实现类实现多个接口，需要提供
    Class<?> interfaceClass() default void.class;

    String version() default "";
}
