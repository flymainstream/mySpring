package com.js.v3.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author name
 * @date 2020/12/9
 * @dateTime 22:49
 * @description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MyComponent
public @interface MyService {
    String value() default "";
}
