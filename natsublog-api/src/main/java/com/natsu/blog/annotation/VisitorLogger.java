package com.natsu.blog.annotation;

import com.natsu.blog.enums.VisitorBehavior;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面注解，用于记录访问日志
 *
 * @author NatsuKaze
 * @since 2023/1/19 加入注释
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitorLogger {

    /**
     * 默认行为：unknown
     *
     * @return {@link VisitorBehavior}
     */
    VisitorBehavior value() default VisitorBehavior.UNKNOWN;

}
