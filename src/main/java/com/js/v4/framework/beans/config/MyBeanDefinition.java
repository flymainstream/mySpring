package com.js.v4.framework.beans.config;

/**
 * @author 刘锦涛
 * @title: MyBeanDefinition
 * @projectName spring
 * @date 2020/12/23
 * @dateTime 18:01
 * @description: TODO
 */

public class MyBeanDefinition {
    private String factoryBeanName;
    private String beanClassName;

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}
