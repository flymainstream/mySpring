package com.js.v4.framework.aop;

import com.js.v4.framework.aop.aspect.MyAdvice;
import com.js.v4.framework.aop.support.MyAdviceSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 刘锦涛
 * @title: jdkDynamicAopProxy
 * @projectName spring
 * @date 2021/1/4
 * @dateTime 18:50
 * @description: TODO
 */
public class MyJdkDynamicAopProxy implements InvocationHandler {

    private MyAdviceSupport adviceConfig;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, MyAdvice> adviceMap = adviceConfig.getAdivces(method, null);
        try {
//        adviceMap.get("before").invoke;

            method.invoke(null, args);

//        adviceMap.get("after").invoke;
        } catch (Exception e) {
//        adviceMap.get("aspectAfterThrow").invoke;

        }

        return null;
    }

    public Object getProxyInstance() {
        return null;
    }
}
