package com.js.v4.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author 刘锦涛
 * @title: MyAdvice
 * @projectName spring
 * @date 2021/1/4
 * @dateTime 18:46
 * @description: TODO
 */
public class MyAdvice {
    private Object aspect;
    private Method adviceMethod;
    private String aspectAfterThrowingName;

    public String getAspectAfterThrowingName() {
        return aspectAfterThrowingName;
    }

    public Object getAspect() {
        return aspect;
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }
}
