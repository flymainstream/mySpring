package com.js.v5.framework.context.v4.framework.web.mvc.servlet;


import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author name
 * @date 2020/12/28
 * @dateTime 22:58
 * @description:
 */

public class MyHandlerMapping {

    private Pattern url;
    private Method method;
    private Object methodOfInstance;

    public MyHandlerMapping(Pattern url, Method method, Object methodOfInstance) {
        this.url = url;
        this.method = method;
        this.methodOfInstance = methodOfInstance;
    }

    public Pattern getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public Object getMethodOfInstance() {
        return methodOfInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyHandlerMapping that = (MyHandlerMapping) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        return methodOfInstance != null ? methodOfInstance.equals(that.methodOfInstance) : that.methodOfInstance == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (methodOfInstance != null ? methodOfInstance.hashCode() : 0);
        return result;
    }
}
