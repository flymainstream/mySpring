package com.js.v5.framework.aop.aspect;

import com.js.v5.framework.aop.intercept.JoinPoint;
import com.js.v5.framework.aop.intercept.MyJoinPoint;
import com.js.v5.framework.aop.intercept.MyMethodIntercept;

/**
 * @author 刘锦涛
 * @title: MyMethodBeforeIntercept
 * @projectName spring
 * @date 2021/1/27
 * @dateTime 20:49
 * @description: TODO
 */
public class MyMethodAfterIntercept implements MyMethodIntercept {
    @Override
    public Object invoke(MyJoinPoint mi) {

        return mi.proceed();
    }
}
