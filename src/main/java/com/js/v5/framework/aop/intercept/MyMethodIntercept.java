package com.js.v5.framework.aop.intercept;

/**
 * @author 刘锦涛
 * @title: MyMthodInvocation
 * @projectName spring
 * @date 2021/1/27
 * @dateTime 20:12
 * @description: TODO
 */
public interface MyMethodIntercept {


    Object invoke(MyJoinPoint myMethodInvocation);
}
