package com.js.v3.framework.beans.suppot;

import com.js.v3.framework.beans.config.MyBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * @author 刘锦涛
 * @title: MyBeanDefinition
 * @projectName spring
 * @date 2020/12/23
 * @dateTime 18:05
 * @description: TODO
 */
public class MyBeanDefinitionReader {

    private String[] configLocations;
    private List<String> registerBeanNames = new ArrayList<String>(24);
    private Properties contextConfig = new Properties();

    public MyBeanDefinitionReader(String[] configLocations) {
//        this.configLocations = configLocations;
        this.loadConfiguration(configLocations[0]);
        this.classScanning(contextConfig.getProperty("classSrc"));
    }

    public MyBeanDefinition loadBeanDefinition() {

        List<MyBeanDefinition> myBeanDefinitions = new ArrayList<>();
        registerBeanNames.forEach(className -> {
            try {
                Class<?> beanClass = Class.forName(className);
                //                1. 默认首字母小写
                MyBeanDefinition myBeanDefinition = getMyBeanDefinition(
                        toLowerFirstCase(beanClass.getSimpleName())
                        , beanClass.getName());

//                2. 自定义

                myBeanDefinitions.add(myBeanDefinition);
//                3. 接口注入
                for (Class<?> anInterface : beanClass.getInterfaces()) {

                    myBeanDefinitions.add(getMyBeanDefinition(
                            toLowerFirstCase(anInterface.getName())
                            , beanClass.getName()));
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    private MyBeanDefinition getMyBeanDefinition(String beanName, String className) {
        MyBeanDefinition myBeanDefinition = new MyBeanDefinition();
//               保存className
        myBeanDefinition.setBeanClassName(className);

//                保存BeanName

        myBeanDefinition.setFactoryBeanName(beanName);
        return myBeanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void classScanning(String pagePath) {

        URL resource = this.getClass().getClassLoader().getResource("/" + pagePath.replaceAll("\\.", "/"));

        File file = new File(resource.getFile());

        for (File listFile : file.listFiles()) {

            if (listFile.isDirectory()) {
                classScanning(pagePath + "." + listFile.getName());
            } else {
//                        为null报错
                Optional<String> fName = Optional.of(listFile.getName());
//                只筛选 是以 class结尾的 ,如果非class 结尾 返回 null
                fName
//                        判断是否 以 .class 皆为
                        .filter(name ->
//                                此处返回false 那么 filter 将会返回 null
                                name.endsWith(".class"))
                        .ifPresent(name -> {
                            //          替换掉class
                            String className = pagePath + "." + name.replace(".class", "");
                            registerBeanNames.add(className);
                        });


            }
        }


    }

    private void loadConfiguration(String config) {

        config = config.replace("classpath:", "").trim();

        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(config);
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            Optional<InputStream> ois = Optional.ofNullable(is);

            ois.ifPresent(e -> {
                try {
                    e.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

        }
    }


}
