package com.js.v5.framework.aop.aspect;

import com.js.v5.framework.aop.intercept.MyJoinPoint;

import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 刘锦涛
 * @title: MyAbstractAspectAdvice
 * @projectName spring
 * @date 2021/1/27
 * @dateTime 20:53
 * @description: TODO
 */
public abstract class MyAbstractAspectAdvice implements MyAdvice {
    private Object aspect;
    private Method adviceMethod;
    private String aspectAfterThrowingName;

    public MyAbstractAspectAdvice(Object aspect, Method adviceMethod, String aspectAfterThrowingName) {
        this.aspect = aspect;
        this.adviceMethod = adviceMethod;
        this.aspectAfterThrowingName = aspectAfterThrowingName;
    }

    public MyAbstractAspectAdvice(Object aspect, Method adviceMethod) {
        this.aspect = aspect;
        this.adviceMethod = adviceMethod;
    }

    public String getAspectAfterThrowingName() {
        return aspectAfterThrowingName;
    }

    public Object getAspect() {
        return aspect;
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public Object invokeAdviceMethod(
            MyJoinPoint jpMatch, Object returnValue, Throwable ex
    ) throws InvocationTargetException, IllegalAccessException {

        Class<?>[] parameterTypes = this.adviceMethod.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return this.adviceMethod.invoke(aspect);
        }
    }
}
