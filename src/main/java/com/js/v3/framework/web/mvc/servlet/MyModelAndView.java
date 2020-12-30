package com.js.v3.framework.web.mvc.servlet;

import java.util.Map;

/**
 * @author name
 * @date 2020/12/29
 * @dateTime 21:43
 * @description:
 */
public class MyModelAndView {

    private String viewName;
    private Map<String, ?> map;

    public MyModelAndView(String viewName, Map<String, ?> map) {
        this.viewName = viewName;
        this.map = map;
    }

    public MyModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return map;
    }
}
