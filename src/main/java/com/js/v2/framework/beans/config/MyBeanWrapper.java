package com.js.v2.framework.beans.config;

/**
 * @author 刘锦涛
 * @title: MyBeanWrapper
 * @projectName spring
 * @date 2020/12/24
 * @dateTime 20:13
 * @description: TODO
 */
public class MyBeanWrapper {

    private Object wrapperInstance;
    private Class<?> wrapperClass;

    public MyBeanWrapper(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
        this.wrapperClass = wrapperInstance.getClass();
    }

    public Object getInstance() {
        return wrapperInstance;
    }



    public Class<?> getWrapperClass() {
        return wrapperClass;
    }

    public void setWrapperClass(Class<?> wrapperClass) {
        this.wrapperClass = wrapperClass;
    }

    public void setInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }
}
