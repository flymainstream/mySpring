package com.js.v5.framework.context.v4.framework.context;

import com.js.v5.framework.context.v4.framework.annotation.MyAutoWired;
import com.js.v5.framework.context.v4.framework.annotation.MyQualifier;
import com.js.v5.framework.context.v4.framework.aop.MyJdkDynamicAopProxy;
import com.js.v5.framework.context.v4.framework.aop.config.MyAopConfig;
import com.js.v5.framework.context.v4.framework.aop.support.MyAdviceSupport;
import com.js.v5.framework.context.v4.framework.beans.config.MyBeanDefinition;
import com.js.v5.framework.context.v4.framework.beans.config.MyBeanWrapper;
import com.js.v5.framework.context.v4.framework.beans.support.MyBeanDefinitionReader;

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


    private Map<String, MyBeanDefinition> beanDefinitionMap = new HashMap<>(24);
    private MyBeanDefinitionReader beanDefinitionReader;

    /**
     * bean工厂
     */
    private Map<String, MyBeanWrapper> factoryBeanCache = new HashMap<>(24);
    /**
     * 原生对象缓存
     */
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>(24);

    /**
     * 代理 类缓存
     */
    Map<String, MyJdkDynamicAopProxy> proxyCatch = new HashMap<>(24);

    /**
     * 初次出入失败对象的缓存
     */
    private Map<String, MyBeanDefinition> populateAgain = new HashMap(24);

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

        populateAgain.forEach(this::getBean);
    }

    private void registerBeanDefinition(List<MyBeanDefinition> myBeanDefinitions) {
        myBeanDefinitions.forEach(beanDefinition -> {

            String factoryName = beanDefinition.getFactoryBeanName();
            String className = beanDefinition.getBeanClassName();
            if (beanDefinitionMap.containsKey(factoryName)) {
                throw new RuntimeException(" factory name already  exist");
            }

            beanDefinitionMap.put(factoryName, beanDefinition);
            beanDefinitionMap.put(className, beanDefinition);


        });
    }

    public Object getBean(String beanName, MyBeanDefinition beanDefinition) {

//      1. 实例化配置信息

        Object instance = instantiateBean(beanName, beanDefinition);
//      2. AOP 如果满足条件就直接返回AOP 的 proxy 对象
        instance = aopProxy(instance, beanDefinition);
//      3. 封装
        MyBeanWrapper myBeanWrapper = new MyBeanWrapper(instance);
//      4. 丢到Ioc 之中
        factoryBeanCache.put(beanName, myBeanWrapper);
//      5. 执行依赖注入
        populateBean(beanName, beanDefinition, myBeanWrapper);

        return myBeanWrapper.getInstance();
    }

    private Object aopProxy(Object instance, MyBeanDefinition beanDefinition) {

//        1. 加载AOP 的配置文件
        MyAdviceSupport config = instanceAopConfig();
        if (config.getConfig().getPointCut().length() < 1) {
            return instance;

        }
        config.setTarget(instance);
        config.setTargetClass(instance.getClass());
//        2. 判断是否需要生成代理类
        if (config.pointCutMath()) {


            if (proxyCatch.containsKey(beanDefinition.getBeanClassName())) {
                return proxyCatch.get(beanDefinition.getBeanClassName()).getProxyInstance();
            }
            MyJdkDynamicAopProxy aopProxy = new MyJdkDynamicAopProxy(config);
            proxyCatch.put(beanDefinition.getBeanClassName(), aopProxy);
            return aopProxy.getProxyInstance();
        }
        return instance;
    }

    private MyAdviceSupport instanceAopConfig() {
        MyAopConfig aopConfig = new MyAopConfig();
        aopConfig.setAspectAfter(this.beanDefinitionReader.getConfig().getProperty("aspectAfter"));
        aopConfig.setAspectClass(this.beanDefinitionReader.getConfig().getProperty("aspectClass"));
        aopConfig.setAspectBefore(this.beanDefinitionReader.getConfig().getProperty("aspectBefore"));

        aopConfig.setAspectAfterThrow(this.beanDefinitionReader.getConfig().getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(this.beanDefinitionReader.getConfig().getProperty("aspectAfterThrowingName"));
        aopConfig.setPointCut(this.beanDefinitionReader.getConfig().getProperty("pointCut"));

        return new MyAdviceSupport(aopConfig);
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

//        if (!beanClass.isAnnotationPresent(MyComponent.class)) {
//            return;
//        }

        for (Field field : beanClass.getDeclaredFields()) {

            if (!field.isAnnotationPresent(MyAutoWired.class)) {
                continue;
            }

            String qualifierBeanName = getUserSetBeanName(field);
            if ("".equals(qualifierBeanName)) {
                qualifierBeanName = field.getType().getName();
            }
            if (factoryBeanCache.get(qualifierBeanName) == null) {
                populateAgain.put(qualifierBeanName, beanDefinition);
                return;
            }

            field.setAccessible(true);
            try {

                field.set(instance, factoryBeanCache.get(qualifierBeanName).getInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }


    }

    private String getUserSetBeanName(Field field) {
        String qualifierBeanName = "";
        MyQualifier qualifier = field.getClass().getAnnotation(MyQualifier.class);
        if (qualifier != null) {
            qualifierBeanName = qualifier.value().trim();
        }
        return qualifierBeanName;
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

    public Properties getConfig() {

        return this.beanDefinitionReader.getConfig();
    }
}
