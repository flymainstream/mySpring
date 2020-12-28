package com.js.v2.framework.web.mvc.servlet;

import com.js.v1.*;
import com.js.v2.framework.context.MyApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author name
 * @date 2020/12/9
 * @dateTime 20:43
 * @description:
 */

public class DispatchServlet extends HttpServlet {
    private MyApplicationContext applicationContext;




    private Map<String, Object> handlerMapping = new HashMap<>(24);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//      委派method  返回response
        try {
            dispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException {
        String url = getUrl(req);

        if (!this.handlerMapping.containsKey(url)) {
            try {
                resp.getWriter().write(" 404 Not Found !!! ");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, String[]> parameterMap = req.getParameterMap();
        Method method = (Method) this.handlerMapping.get(url);

        String name = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        Object o = applicationContext.getBean(name);

        method.invoke(o, new Object[]{
                req, resp, parameterMap.get("name")[0]
        });
    }

    private String getUrl(HttpServletRequest req) {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        return url.replaceAll(contextPath, "").replaceAll("/+", "/");
    }


    @Override
    public void init(ServletConfig config) throws ServletException {


        Optional<ServletConfig> sco = Optional.ofNullable(config);
        this.applicationContext = new MyApplicationContext(sco.orElseThrow(this::ThrowRunTime).getInitParameter("application"));


        System.out.println(" finish init My Spring ");
    }










    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }



    public RuntimeException ThrowRunTime() {

        return new RuntimeException(" you're can't play with mySpring .Because you didn't have any config file  ");
    }
}
