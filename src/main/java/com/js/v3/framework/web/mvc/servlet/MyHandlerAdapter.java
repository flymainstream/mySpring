package com.js.v3.framework.web.mvc.servlet;

import com.js.v3.framework.annotation.MyRequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author name
 * @date 2020/12/29
 * @dateTime 22:21
 * @description:
 */
public class MyHandlerAdapter {

    public MyModelAndView handler(HttpServletRequest req, HttpServletResponse resp, MyHandlerMapping handler) throws Exception {

        /*
         * 保存形参列表
         * 将参数的名称和参数的位置 关系保存起来
         * */
        Map<String, Integer> paramIndexCathe = new HashMap<>();

        Map<String, String[]> parameterMap = req.getParameterMap();
        Method method = handler.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {

                if (!(annotation instanceof MyRequestParam)) {
                    continue;
                }
                String paramName = ((MyRequestParam) annotation).value();
                if ("".equals(paramName)) {
                    continue;
                }
                paramIndexCathe.put(paramName, i);


            }


        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];

            if (parameterType == HttpServletRequest.class) {
                paramIndexCathe.put(parameterType.getName(), i);
            } else if (parameterType == HttpServletResponse.class) {
                paramIndexCathe.put(parameterType.getName(), i);
            } else {


            }
        }

        /*
         * 拼接实参
         * */
        Object[] paramValues = new Object[parameterTypes.length];


        parameterMap.entrySet().forEach(entry -> {
            String value = Arrays
                    .toString(entry.getValue())
                    .replaceAll("\\[|\\]", "")
                    .replaceAll("\\s+", ",");

            if (!paramIndexCathe.containsKey(entry.getKey())) {
                return;
            }
            Integer index = paramIndexCathe.get(entry.getKey());


            Object o = caseValue(value, parameterTypes[index]);
            /* 直接赋值 , 就有可能是自己的 定义的类, 需要转换*/
            paramValues[index] = o;


        });

        if (paramIndexCathe.containsKey(HttpServletRequest.class.getName())) {
            paramValues[paramIndexCathe.get(HttpServletRequest.class.getName())]
                    = req;
        }

        if (paramIndexCathe.containsKey(HttpServletResponse.class.getName())) {
            paramValues[paramIndexCathe.get(HttpServletResponse.class.getName())]
                    = resp;
        }

        Object result = handler.getMethod().invoke(handler.getMethodOfInstance(), paramValues);

        /*
         * 返回值有两种情况
         * */
        if (result == null || result instanceof Void) {
            return null;
        }
        /*
         * 返回值是JSON 加了responseBody 转json
         * */

        /*
        * 返回值 是 ModeAndView
        * */
        Class<?> returnType = handler.getMethod().getReturnType();
        if (returnType == MyModelAndView.class) {
            return (MyModelAndView) result;
        }
        return null;

    }

    private Object caseValue(String value, Class<?> parameterType) {

        if (String.class == parameterType) {
            return value;

        } else if (Integer.class == parameterType) {
            return Integer.valueOf(value);

        } else {
            throw new RuntimeException(" 暂不支持 !!!!!............ ");
        }

    }
}
