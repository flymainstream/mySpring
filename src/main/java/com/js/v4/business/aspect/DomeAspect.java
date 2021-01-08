package com.js.v4.business.aspect;

/**
 * @author 刘锦涛
 * @title: DomeAspect
 * @projectName spring
 * @date 2021/1/8
 * @dateTime 18:00
 * @description: TODO
 */
public class DomeAspect {

    public void before() {
        System.out.println(" do that on before method ");

    }

    public void after() {
        System.out.println(" do that on after method ");

    }

    public void error() {
        System.out.println(" do that on error method ");

    }
}
