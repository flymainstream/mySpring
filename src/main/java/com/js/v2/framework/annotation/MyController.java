package com.js.v2.framework.annotation;

import java.lang.annotation.*;

/**
 * @author name
 * @date 2020/12/9
 * @dateTime 22:49
 * @description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MyComponent
@Documented
public @interface MyController  {
    String value() default "";
}
