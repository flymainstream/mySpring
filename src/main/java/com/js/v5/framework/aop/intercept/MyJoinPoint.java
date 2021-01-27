package com.js.v5.framework.aop.intercept;

import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @author 刘锦涛
 * @title: JoinPoint
 * @projectName spring
 * @date 2021/1/27
 * @dateTime 20:17
 * @description: TODO
 */
public interface MyJoinPoint {

    @Nullable
    public Object proceed() throws Throwable;


    @Nullable
    public Object getThis() throws Throwable;

    @Nullable
    public Object[] getArguments() throws Throwable;

    @Nullable

    public Method getMethod() throws Throwable;


    public Object getUserAttribute(String key) throws Throwable;

    public void setUserAttribute(String key, Object value) throws Throwable;


}
