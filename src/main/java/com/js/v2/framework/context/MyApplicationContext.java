package com.js.v2.framework.context;

import com.js.v2.framework.beans.config.MyBeanDefinition;
import com.js.v2.framework.beans.suppot.MyBeanDefinitionReader;

import java.util.Map;

/**
 * @author 刘锦涛
 * @title: MyApplicationContext
 * @projectName spring
 * @date 2020/12/23
 * @dateTime 17:44
 * @description: 职责 create object and DI
 */
public class MyApplicationContext {

    private String[] configLocations;

    private Map<String, MyBeanDefinition> beanDefinitionMap;
    private MyBeanDefinitionReader beanDefinitionReader;

    public MyApplicationContext(String... configLocations) {

//        1. 加载配置文件
        this.beanDefinitionReader = new MyBeanDefinitionReader(configLocations);
//        2. 解析配置文件 变成BeanDefinition
        MyBeanDefinition myBeanDefinition = beanDefinitionReader.loadBeanDefinition();
//        3. 缓存 BeanDefinition
        registerBeanDefinition();
//        4. DI
        autoWired();

    }

    private void autoWired() {
    }

    private void registerBeanDefinition() {
    }

    public Object getBean(String name) {

        return null;
    }

    public Object getBean(Class<?> clazz) {

        return this.getBean(clazz.getName());
    }

}
