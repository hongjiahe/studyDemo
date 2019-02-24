package com.hohe.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义操作日志注解
 */
@Retention(RetentionPolicy.RUNTIME) // 在运行时可以获取
@Target(value = {ElementType.METHOD, ElementType.TYPE}) // 作用到类，方法，接口上等
public @interface SysOperationLog {

    /**
     * 操作名称
     */
    String functionName() default "";

}
