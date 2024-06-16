package com.natsu.blog.annotation;

import com.natsu.blog.enums.OperationTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面注解，用于记录操作日志
 *
 * @author NatsuKaze
 * @since 2024/06/16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogger {

    /**
     * 操作类型
     * @return Integer
     */
    OperationTypeEnum type();

    /**
     * 操作描述
     * @return String
     */
    String description() default "";

}
