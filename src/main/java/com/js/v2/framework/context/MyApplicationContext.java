package com.js.v2.framework.context;

import com.js.v2.framework.annotation.MyAutoWired;
import com.js.v2.framework.annotation.MyComponent;
import com.js.v2.framework.annotation.MyController;
import com.js.v2.framework.annotation.MyQualifier;
import com.js.v2.framework.beans.config.MyBeanDefinition;
import com.js.v2.framework.beans.config.MyBeanWrapper;
import com.js.v2.framework.beans.suppot.MyBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 刘锦涛
 * @title: MyApplicationContext
 * @projectName spring
 * @date 2020/12/23
 * @dateTime 17:44
 * @description: 职责 create object and DI
 */
public class MyApplicationContext {

    private MyBeanDefinitionReader beanDefinitionReader;

    /**
     * bean的定义 map
     */
    private Map<String, MyBeanDefinition> beanDefinitionMap = new HashMap<>(24);
    /* bean的 包装增强map */
    private Map<String, MyBeanWrapper> factoryBeanCache = new HashMap<>(24);
    /*  Bean的instance 备份*/
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>(24);


    public MyApplicationContext(String... configLocations) {

//        1. 加载配置文件
        this.beanDefinitionReader = new MyBeanDefinitionReader(configLocations);
//        2. 解析配置文件 变成BeanDefinition
        List<MyBeanDefinition> myBeanDefinitions = beanDefinitionReader.loadBeanDefinition();
//        3. 缓存 BeanDefinition
        registerBeanDefinition(myBeanDefinitions);
//        4. DI
        autoWired();

    }

    private void autoWired() {

        beanDefinitionMap.forEach(this::getBean);
    }

    private void registerBeanDefinition(List<MyBeanDefinition> myBeanDefinitions) {
        myBeanDefinitions.forEach(beanDefinition -> {

            String factoryName = beanDefinition.getFactoryBeanName();
            String className = beanDefinition.getBeanClassName();
            if (beanDefinitionMap.containsKey(factoryName)) {
                throw new RuntimeException(" factory name already  exist");
            }
            if (beanDefinitionMap.containsKey(className)) {
                throw new RuntimeException(" class name already  exist");
            }

            beanDefinitionMap.put(factoryName, beanDefinition);
            beanDefinitionMap.put(className, beanDefinition);


        });
    }

    public Object getBean(String beanName, MyBeanDefinition beanDefinition) {

//      1. 实例化配置信息

        Object instance = instantiateBean(beanName, beanDefinition);
//      2. 封装
        MyBeanWrapper myBeanWrapper = new MyBeanWrapper(instance);
//      3. 丢到Ioc 之中
        factoryBeanCache.put(beanName, myBeanWrapper);
//      4. 执行依赖注入
        populateBean(beanName, beanDefinition, myBeanWrapper);

        return myBeanWrapper.getInstance();
    }

    /**
     * @param beanName
     * @param beanDefinition
     * @param beanWrapper
     */
    private void populateBean(String beanName, MyBeanDefinition beanDefinition, MyBeanWrapper beanWrapper) {
//        可能会涉及到循环依赖
//        什么是循环依赖 A 里面需要注入B  B里面需要注入A
//
        Object instance = beanWrapper.getInstance();

        Class<?> beanClass = beanWrapper.getWrapperClass();


        /* 在 get 属性的时候 未使用 getDeclaredFields 而是使用了 getFields */
        for (Field field : beanClass.getDeclaredFields()) {

            if (!field.isAnnotationPresent(MyAutoWired.class)) {
                continue;
            }

            MyQualifier qualifier = field.getClass().getAnnotation(MyQualifier.class);
            /* 未做 null 判断 */
            String qualifierBeanName = qualifier == null ? "" : qualifier.value().trim();
            if ("".equals(qualifierBeanName)) {
                qualifierBeanName = field.getType().getName();
            }


            field.setAccessible(true);
            try {
                field.set(instance, factoryBeanCache.get(qualifierBeanName).getInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * 真正创建对象
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object instantiateBean(String beanName, MyBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
//        将代码变成单例
        if (this.factoryBeanObjectCache.containsKey(beanName)) {
            return this.factoryBeanObjectCache.get(beanName);
        }
        try {
            Class<?> aClass = Class.forName(className);

            instance = aClass.getConstructor().newInstance();
//            创建备份
            this.factoryBeanObjectCache.put(beanName, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return instance;
    }

    public Object getBean(String key) {


        return this.getBean(key, beanDefinitionMap.get(key));
    }


    public Object getBean(Class<?> clazz) {

        return this.getBean(clazz.getName());
    }

    /**
     * 获得Ioc里面有多少个bean
     *
     * @return
     */
    public int getBeanDefinitionCounts() {

        return this.beanDefinitionMap.size();
    }

    public Set<String> getBeanDefinitionNames() {

        return this.beanDefinitionMap.keySet();
    }


}
