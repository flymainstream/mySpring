package com.js.v4.framework.aop;

import com.js.v4.framework.aop.aspect.MyAdvice;
import com.js.v4.framework.aop.support.MyAdviceSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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

    public MyJdkDynamicAopProxy(MyAdviceSupport config) {
        adviceConfig = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, MyAdvice> adviceMap = adviceConfig.getAdivces(method );

        Object value = null;

        try {
            invokeAdvice(adviceMap.get("before"));

            value = method.invoke(adviceConfig.getTarget(), args);

            invokeAdvice(adviceMap.get("after"));
        } catch (Exception e) {
            invokeAdvice(adviceMap.get("aspectAfterThrow"));
            throw e;
        }

        return value;
    }

    private void invokeAdvice(MyAdvice advice) throws InvocationTargetException, IllegalAccessException {
        if (advice == null) {
            return;
        }
        advice.getAdviceMethod().invoke(
                advice.getAspect()
        );

    }

    public Object getProxyInstance() {
        return Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                this.adviceConfig.getTargetClass().getInterfaces(),
                this);
    }

}
