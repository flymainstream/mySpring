package com.js.v5.framework.context.v4.framework.aop.config;

/**
 * @author 刘锦涛
 * @title: MyAopConfig
 * @projectName spring
 * @date 2021/1/4
 * @dateTime 17:42
 * @description: TODO
 */
public class MyAopConfig {

    String pointCut;
    String aspectClass;
    String aspectBefore;
    String aspectAfter;
    String aspectAfterThrow;
    String aspectAfterThrowingName;

    public void setAspectClass(String aspectClass) {
        this.aspectClass = aspectClass;
    }

    public void setAspectBefore(String aspectBefore) {
        this.aspectBefore = aspectBefore;
    }

    public void setAspectAfter(String aspectAfter) {
        this.aspectAfter = aspectAfter;
    }

    public void setAspectAfterThrow(String aspectAfterThrow) {
        this.aspectAfterThrow = aspectAfterThrow;
    }

    public void setAspectAfterThrowingName(String aspectAfterThrowingName) {
        this.aspectAfterThrowingName = aspectAfterThrowingName;
    }

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public String getPointCut() {
        return pointCut;
    }

    public String getAspectClass() {
        return aspectClass;
    }

    public String getAspectBefore() {
        return aspectBefore;
    }

    public String getAspectAfter() {
        return aspectAfter;
    }

    public String getAspectAfterThrow() {
        return aspectAfterThrow;
    }

    public String getAspectAfterThrowingName() {
        return aspectAfterThrowingName;
    }
}
