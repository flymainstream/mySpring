package com.js.v5.framework.context.v4.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 刘锦涛
 * @title: MyAutoWired
 * @projectName spring
 * @date 2020/12/17
 * @dateTime 15:06
 * @description: TODO
 */
/*
 * ElementType.FIELD 属性
 * ElementType.TYPE 类
 * ElementType.METHOD 函数
 * */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutoWired {

}
