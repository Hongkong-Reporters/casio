package com.report.casio.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
// 消费者注入
public @interface Reference {
    Class<?> interfaceClass() default void.class;
}
