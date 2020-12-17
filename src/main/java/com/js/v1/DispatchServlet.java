package com.js.v1;

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

    Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<String>(24);
    //    key 是类名首字母小写, key 是new的对象
    private Map<String, Object> IoC = new HashMap<>(24);


    private Map<String, Object> handlerMapping = new HashMap<>(24);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
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
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        String key = url.replaceAll(contextPath, "").replaceAll("/+", "/");

        if (!this.handlerMapping.containsKey(key)) {
            try {
                resp.getWriter().write(" 404 Not Found !!! ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, String[]> parameterMap = req.getParameterMap();
        Method method = (Method) this.handlerMapping.get(key);

        String name = method.getDeclaringClass().getSimpleName();
        Object o = IoC.get(toLowerFirstCase(name));

        method.invoke(o, new Object[]{
                req, resp, parameterMap.get("name")[0]
        });
    }


    @Override
    public void init(ServletConfig config) throws ServletException {

        Optional<ServletConfig> sco = Optional.ofNullable(config);
//        1. 加载配置文件
        loadConfiguration(sco.orElseThrow(this::ThrowRunTime).getInitParameter("application"));
//        2. 扫描相关的类
        classScann(contextConfig.getProperty("classSrc"));
//        3. 初始化 IOC 容器
        instance();
//        ================== AOP ===============
        /*
         * 加入时机 在IOC 之后 因为在IOC 之前不知道有多少需要 被AOP
         * 在DI 之后 那么AOP 的对象没有 值  代理对象就是空的
         */
//        4. DI 注入依赖
        autoWired();
//        5. 分发依赖 反射调用
        handlerMapping();

        System.out.println(" finsh init My Spring ");
    }

    private void handlerMapping() {


        if (IoC.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : IoC.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(MyController.class)) {
                continue;
            }
            String classUrl = aClass.getAnnotation(MyController.class).value();


            for (Method method : aClass.getMethods()) {

                String methodUrl = method.getAnnotation(MyRequestMapping.class).value();

                String key;


                key = classUrl + "/" + methodUrl.replaceAll("/+", "/");

                handlerMapping.put(key, method);
            }


        }
    }

    private void autoWired() {

        for (Map.Entry<String, Object> entry : IoC.entrySet()) {

            Class<?> aClass = entry.getValue().getClass();
            for (Field field : aClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(MyAutoWired.class)) {
                    continue;
                }
                String name;

                String value = field.getAnnotation(MyQualifier.class).value();
                if (value.trim().length() > 0) {
                    name = value.trim();
                } else {
                    /* 获得当前字段 的 类型 获得 类全名 , 默认通过类全名进行获取
                     * 需要注入的对象
                     * */
                    name = toLowerFirstCase(field.getType().getName());

                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), IoC.get(name));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }


        }


    }

    private void instance() {
        Optional<List<String>> cnos = Optional.ofNullable(this.classNames);
        cnos.ifPresent(this::getListConsumer);

    }

    private void getListConsumer(List<String> names) {

        names.forEach(this::handlerObjCreate);
    }

    private void handlerObjCreate(String element) {

        try {
            Class<?> aClass = Class.forName(element);
            Optional<? extends Class<?>> classOptional = Optional.ofNullable(aClass);
//                        此处判断  不控制没有 注解的 对象
            classOptional.filter(
                    eClass -> {

                        return eClass.isAnnotationPresent(MyController.class)
                                ||
                                eClass.isAnnotationPresent(MyService.class);
                    }
            ).ifPresent(eClass -> {
                String simpleName = aClass.getSimpleName();

                /* 用户在注解上自定义 value Name 以用户定义为主*/
                String userDefinedValue = eClass.getAnnotation(MyService.class).value();
                if (userDefinedValue.length() > 0) {
                    simpleName = userDefinedValue.trim();
                }

                Object o = null;
                try {
                    o = eClass.getConstructor().newInstance();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                simpleName = toLowerFirstCase(simpleName);
                IoC.put(simpleName, o);

                /* 情况 一个接口下面 有多个实现类, 判断
                 * 其他实现类是否 自定义服务名称
                 * */
                for (Class<?> anInterface : eClass.getInterfaces()) {
//                                interface 类名 +上自定义名称 如果有重复证明当前 自定义名称有问题
                    String key = anInterface.getName();
                    if (IoC.containsKey(key)) {
                        throw new RuntimeException(" One interface One  ");
                    }
                    IoC.put(key, o);

                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void classScann(String pagePath) {

        URL resource = this.getClass().getClassLoader().getResource("/" + pagePath.replaceAll("\\.", "/"));

        File file = new File(resource.getFile());

        for (File listFile : file.listFiles()) {

            if (listFile.isDirectory()) {
                classScann(pagePath + "." + listFile.getName());
            } else {
                if (!listFile.getName().endsWith(".class")) {
                    continue;
                }
                Optional<String> fName = Optional.ofNullable(listFile.getName());
//                只筛选 是以 class结尾的 ,如果非class 结尾 返回 null
                fName = fName.filter(name -> file.getName().endsWith(".class"));
//                如果 fName 不为 null
                fName.ifPresent(name -> {
                    //          替换掉class
                    String className = pagePath + "." + name.replace(".class", "");
                    classNames.add(className);
                });


            }
        }


    }

    private void loadConfiguration(String config) {

        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(config);
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            Optional<InputStream> ois = Optional.ofNullable(is);

            ois.ifPresent(e -> {
                try {
                    e.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

        }
    }

    public RuntimeException ThrowRunTime() {

        return new RuntimeException(" you're can't play with mySpring .Because you didn't have any config file  ");
    }
}
