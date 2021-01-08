package com.js.v4.business.scanner.controller;

import com.js.v4.business.service.DomeService;
import com.js.v4.framework.annotation.MyAutoWired;
import com.js.v4.framework.annotation.MyController;
import com.js.v4.framework.annotation.MyRequestMapping;
import com.js.v4.framework.annotation.MyRequestParam;
import com.js.v4.framework.web.mvc.servlet.MyModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author name
 * @date 2020/12/29
 * @dateTime 21:46
 * @description:
 */
@MyController()
public class DomeController {

    /**
     * 这里 如果不给 接口 那么就会导致在注入的时候 注入不进来 因为AOP时 用的是 jdk 接口动态代理
     * <p>
     * 如果给 接口 就会导致在AOP 的时候AOP失败
     */
    @MyAutoWired
    private DomeService service;

    @MyRequestMapping("/dome*")
    public MyModelAndView dome() {

        Map<String, String> echoMap = new HashMap<>();
        service.handler("123");
        echoMap.put("Jimmy", "Jimmy");
        echoMap.put("Sophie", "Sophie");
        return new MyModelAndView("index", echoMap);
    }

    @MyRequestMapping("/other.love")
    public MyModelAndView makeSimple(
            @MyRequestParam("name") String name
            , HttpServletRequest request
            , HttpServletResponse response
    ) {

        HashMap<String, String> echoMap = new HashMap<>();

        echoMap.put("name", name);
        echoMap.put("Jimmy", "Jimmy");
        echoMap.put("Sophie", "Sophie");
        return new MyModelAndView("more", echoMap);
    }
}
