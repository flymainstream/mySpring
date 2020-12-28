package com.js.v3.framework.web.mvc.servlet;


import java.lang.reflect.Method;

/**
 * @author name
 * @date 2020/12/28
 * @dateTime 22:58
 * @description:
 */

public class MyHandlerMapping {

    private String url;
    private Method method;
    private Object methodOfInstance;

    public MyHandlerMapping(String url, Method method, Object methodOfInstance) {
        this.url = url;
        this.method = method;
        this.methodOfInstance = methodOfInstance;
    }

    public String getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public Object getMethodOfInstance() {
        return methodOfInstance;
    }
}
