package com.js.v5.framework.context.v4.framework.aop.support;

import com.js.v5.framework.context.v4.framework.aop.aspect.MyAdvice;
import com.js.v5.framework.context.v4.framework.aop.config.MyAopConfig;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 刘锦涛
 * @title: MyAdviceSuuopt
 * @projectName spring
 * @date 2021/1/4
 * @dateTime 18:49
 * @description: TODO
 */
public class MyAdviceSupport {
    private MyAopConfig config;
    private Class<?> targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

//    private Map<Method, Map<String, MyAdvice>> methodCatch = new HashMap<>(24);
    private Map<Method, List<Object>> methodCatch = new HashMap<>(24);

    public MyAdviceSupport(MyAopConfig aopConfig) {
        config = aopConfig;
    }

   /* public Map<String, MyAdvice> getAdivces(Method method) throws NoSuchMethodException {
        Map<String, MyAdvice> cached = methodCatch.get(method);
        if (cached == null) {
            Method tempMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCatch.get(tempMethod);
        }

        return cached==null ? new HashMap<>() : cached;
    }
*/
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, @Nullable Class<?> targetClass) {

    }


    public boolean pointCutMath() {

        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    public void setConfig(MyAopConfig config) {
        this.config = config;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
//        拿到类名后开始解析
        parse();
    }

    private void parse() {
        String point = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

//        解析 point分为三段
//        1. 方法的修饰符和返回值
//        2. 类名
        String pointCutClassRegex = point.substring(0, point.lastIndexOf("\\(") - 4);
        pointCutClassRegex =
                "class " + pointCutClassRegex.substring(pointCutClassRegex.lastIndexOf(" ") + 1);
        pointCutClassPattern = Pattern.compile(pointCutClassRegex);
//        3. 方法的名称和新参列表
        Pattern pointCutPatten = Pattern.compile(point);

        Class<?> aspectClass = null;
        try {
            aspectClass = Class.forName(this.config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();

            for (Method method : aspectClass.getMethods()) {
                aspectMethods.put(aspectClass.getName()+"."+method.getName(), method);
            }


            for (Method method : this.targetClass.getMethods()) {
                Map<String, MyAdvice> adviceMap = new HashMap<>();
                String mo = method.toString();
                if (mo.contains("throws")) {
                    mo = mo.substring(0, mo.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pointCutPatten.matcher(mo);

                if (!matcher.matches()) {
                    continue;
                }
                String aspect = config.getAspectBefore();
                if (null != aspect && aspect.trim().length() > 0) {
                    adviceMap.put("before", new MyAdvice(
                            aspectClass.getConstructor().newInstance()
                            , aspectMethods.get(aspect)
                    ));
                }
                aspect = config.getAspectAfter();
                if (null != aspect && aspect.trim().length() > 0) {
                    adviceMap.put("after", new MyAdvice(
                            aspectClass.getConstructor().newInstance()
                            , aspectMethods.get(aspect)
                    ));
                }

                aspect = config.getAspectAfterThrow();
                if (null != aspect && aspect.trim().length() > 0) {
                    adviceMap.put("aspectAfterThrow", new MyAdvice(
                            aspectClass.getConstructor().newInstance()
                            , aspectMethods.get(aspect)
                            , config.getAspectAfterThrowingName()
                    ));
                }

                this.methodCatch.put(method, adviceMap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public boolean pointCutMatch() {

        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    public MyAopConfig getConfig() {
        return config;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public Pattern getPointCutClassPattern() {
        return pointCutClassPattern;
    }


}
