package com.js.v2;

import com.js.v2.business.DomeController;
import com.js.v2.framework.annotation.MyController;

/**
 * @author 刘锦涛
 * @title: Test
 * @projectName spring
 * @date 2020/12/29
 * @dateTime 16:26
 * @description: TODO
 */
public class Test {
    public static void main(String[] args) {
        Object domeControllerClass = DomeController.class;
        System.out.println(
                domeControllerClass.getClass().isAnnotationPresent(MyController.class)
        );
        System.out.println(
                DomeController.class.isAnnotationPresent(MyController.class)
        );
    }
}
