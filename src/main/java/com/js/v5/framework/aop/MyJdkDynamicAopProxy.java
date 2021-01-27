package com.js.v5.framework.aop;

import com.js.v5.framework.aop.intercept.MyMethodInvocation;
import com.js.v5.framework.context.v4.framework.aop.aspect.MyAdvice;
import com.js.v5.framework.context.v4.framework.aop.support.MyAdviceSupport;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AopInvocationException;
import org.springframework.aop.RawTargetAccess;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @author 刘锦涛
 * @title: jdkDynamicAopProxy
 * @projectName spring
 * @date 2021/1/4
 * @dateTime 18:50
 * @description: TODO
 */
public class MyJdkDynamicAopProxy implements MyAopProxy, InvocationHandler {

    private MyAdviceSupport adviceConfig;

    public MyJdkDynamicAopProxy(MyAdviceSupport config) {
        adviceConfig = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        Object retVal;

        // Get the interception chain for this method.
        List<Object> chain = this.adviceConfig.getInterceptorsAndDynamicInterceptionAdvice(method, this.adviceConfig.getTargetClass());


        MyMethodInvocation invocation =
                new MyMethodInvocation(
                        proxy,
                        this.adviceConfig.getTarget()
                        ,method
                        ,args
                        ,this.adviceConfig.getTargetClass()
                        ,chain
                );
        retVal = invocation.proceed();


        return retVal;
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

    @Override
    public Object getProxy() throws Throwable {
        return Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                this.adviceConfig.getTargetClass().getInterfaces(),
                this);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) throws Throwable {
        return null;
    }
}
