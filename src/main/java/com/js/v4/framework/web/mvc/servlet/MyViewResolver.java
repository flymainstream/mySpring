package com.js.v4.framework.web.mvc.servlet;

import java.io.File;

/**
 * @author name
 * @date 2020/12/30
 * @dateTime 21:26
 * @description: 把页面 变成 View 对象
 */
public class MyViewResolver {

    private final String TEMPLATE_END = ".html";

    private File templateDir;

    public MyViewResolver(String templateRoot) {
//        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateDir = new File(templateRoot);
    }


    public MyView getView(String viewName) {
        if (viewName == null || viewName.trim().length() == 0) {
            return null;
        }
        viewName = viewName.endsWith(TEMPLATE_END) ? viewName : viewName + TEMPLATE_END;

        File tempFile = new File((templateDir.getPath() + "/" + viewName).replaceAll("/+", "/"));

        return new MyView(tempFile);
    }
}
