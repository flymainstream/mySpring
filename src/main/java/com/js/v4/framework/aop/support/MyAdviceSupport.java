package com.js.v4.framework.aop.support;

import com.js.v4.framework.aop.aspect.MyAdvice;
import com.js.v4.framework.aop.config.MyAopConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 刘锦涛
 * @title: MyAdviceSuuopt
 * @projectName spring
 * @date 2021/1/4
 * @dateTime 18:49
 * @description: TODO
 */
public class MyAdviceSupport {
    MyAopConfig config;
    Class<?> targetClass;
    Object target;

    private Map<Method, Map<String, MyAdvice>> methodCatch;

    public MyAdviceSupport(MyAopConfig aopConfig) {
        config = aopConfig;
    }

    public Map<String, MyAdvice> getAdivces(Method method, Object o) {
        Map<String, MyAdvice> adviceMap = methodCatch.get(method);

        return null;
    }

    public boolean pointCutMath() {
        return false;
    }

    public void setConfig(MyAopConfig config) {
        this.config = config;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
