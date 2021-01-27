package com.js.v5.framework.aop.intercept;

import org.springframework.lang.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author 刘锦涛
 * @title: MyMethodIntercept
 * @projectName spring
 * @date 2021/1/27
 * @dateTime 20:10
 * @description: TODO
 */
public class MyMethodInvocation implements JoinPoint {

    protected final Object proxy;

    @Nullable
    protected final Object target;

    protected final Method method;

    protected Object[] arguments;

    @Nullable
    private final Class<?> targetClass;

    /**
     * Lazily initialized map of user-specific attributes for this invocation.
     */
    @Nullable
    private Map<String, Object> userAttributes;

    /**
     * List of MethodInterceptor and InterceptorAndDynamicMethodMatcher
     * that need dynamic checks.
     */
    protected final List<?> interceptorsAndDynamicMethodMatchers;

    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke: then the current interceptor.
     */
    private int currentInterceptorIndex = -1;

    public MyMethodInvocation(
            Object proxy, @Nullable Object target, Method method, @Nullable Object[] arguments,
            @Nullable Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    @Override
    public Object proceed() throws Throwable {
        // We start with an index of -1 and increment early.
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return invokeJoinpoint();
        }

        Object interceptorOrInterceptionAdvice =
                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

        if (interceptorOrInterceptionAdvice instanceof MyMethodIntercept) {
            // Evaluate dynamic method matcher here: static part will already have
            // been evaluated and found to match.
            MyMethodIntercept mi =
                    (MyMethodIntercept) interceptorOrInterceptionAdvice;
            Class<?> targetClass = (this.targetClass != null ? this.targetClass : this.method.getDeclaringClass());
            return mi.invoke(this);
            // Dynamic matching failed.
            // Skip this interceptor and invoke the next in the chain.
        } else {
            return proceed();
        }
    }

    private Object invokeJoinpoint() throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(arguments);
    }
}
