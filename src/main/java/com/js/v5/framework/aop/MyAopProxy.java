package com.js.v5.framework.aop;

import java.lang.reflect.InvocationHandler;

/**
 * @author 刘锦涛
 * @title: My
 * @projectName spring
 * @date 2021/1/27
 * @dateTime 20:09
 * @description: TODO
 */
public interface MyAopProxy {


    public Object getProxy() throws Throwable;
    public Object getProxy(ClassLoader classLoader) throws Throwable;


}
