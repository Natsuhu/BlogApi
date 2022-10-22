package com.natsu.blog.annotation;

import com.natsu.blog.enums.VisitorBehavior;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitorLogger {

    VisitorBehavior value() default VisitorBehavior.UNKNOWN;

}
