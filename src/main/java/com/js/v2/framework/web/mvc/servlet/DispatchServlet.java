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

    Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<String>(24);
    //    key 是类名首字母小写, key 是new的对象
    private Map<String, Object> ioC = new HashMap<>(24);


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
        Object o = ioC.get(name);

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

    private void handlerMapping() {
        if (ioC.isEmpty()) {
            return;
        }
        ioC.entrySet().forEach(this::doHandlerMapping);


    }

    private void doHandlerMapping(Map.Entry<String, Object> entry) {
        Class<?> aClass = entry.getValue().getClass();
        if (!aClass.isAnnotationPresent(MyController.class)) {
            return;
        }
        String classUrl = aClass.getAnnotation(MyController.class).value();

        /*
         * 只处理 public 和 有 MyRequestMapping 注解的
         * */
        for (Method method : aClass.getMethods()) {
            MyRequestMapping myRequestMapping = method.getAnnotation(MyRequestMapping.class);
            if (myRequestMapping == null) {
                continue;
            }
            String methodUrl = myRequestMapping.value();
            String key = (classUrl + "/" + methodUrl).replaceAll("/+", "/");
            handlerMapping.put(key, method);
        }


    }

    private void autoWired() {
        if (ioC == null || ioC.size() < 1) {
            return;
        }
        ioC.entrySet().forEach(this::handlerField);

    }

    private void handlerField(Map.Entry<String, Object> entry) {
        Class<?> aClass = entry.getValue().getClass();

        for (Field field : aClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(MyAutoWired.class)) {
                continue;
            }
            String name;
            String value = "";
            if (
                    field.getAnnotation(MyQualifier.class) != null
                            &&

                            setMyQualifierValue(field, value).length() > 0
            ) {
                name = value;
            } else {
                /* 获得当前字段 的 类型 获得 类全名 , 默认通过类全名进行获取
                 * 需要注入的对象
                 * */
                name = toLowerFirstCase(field.getType().getSimpleName());

            }
            field.setAccessible(true);
            try {
                field.set(entry.getValue(), ioC.get(name));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private String setMyQualifierValue(Field field, String value) {
        value = field.getAnnotation(MyQualifier.class).value().trim();
        return value;
    }

    private void instance() {
        Optional<List<String>> cnos = Optional.ofNullable(this.classNames);
        cnos.ifPresent(this::getListConsumer);

    }

    private void getListConsumer(List<String> names) {


        names.forEach(this::handlerObjCreate);
    }

    private void loadClass(String element) {
        try {
            Class.forName(element);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handlerObjCreate(String element) {

        try {
            Class<?> aClass = Class.forName(element);
            Optional<? extends Class<?>> classOptional = Optional.ofNullable(aClass);
//                        此处判断  不控制没有 注解的 对象
            classOptional.filter(
                    eClass ->
                            eClass.isAnnotationPresent(MyController.class)
                                    ||
                                    eClass.isAnnotationPresent(MyService.class)

            ).ifPresent(eClass -> {
                String simpleName = aClass.getSimpleName();

                /* Service处理 用户在注解上自定义 value Name 以用户定义为主*/
                if (eClass.getAnnotation(MyService.class) != null
                        &&
                        eClass.getAnnotation(MyService.class).value().length() > 0) {
                    simpleName = eClass.getAnnotation(MyService.class).value().trim();
                }

                Object o = null;
                try {
                    o = eClass.getConstructor().newInstance();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                simpleName = toLowerFirstCase(simpleName);
                ioC.put(simpleName, o);

                /* 情况 一个接口下面 有多个实现类, 判断
                 * 其他实现类是否 自定义服务名称
                 * */
                for (Class<?> anInterface : eClass.getInterfaces()) {
//                                interface 类名 +上自定义名称 如果有重复证明当前 自定义名称有问题
                    String key = anInterface.getName();
                    if (ioC.containsKey(key)) {
                        throw new RuntimeException(" One interface One  ");
                    }
                    ioC.put(key, o);

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

    private void classScanning(String pagePath) {

        URL resource = this.getClass().getClassLoader().getResource("/" + pagePath.replaceAll("\\.", "/"));

        File file = new File(resource.getFile());

        for (File listFile : file.listFiles()) {

            if (listFile.isDirectory()) {
                classScanning(pagePath + "." + listFile.getName());
            } else {
//                        为null报错
                Optional<String> fName = Optional.of(listFile.getName());
//                只筛选 是以 class结尾的 ,如果非class 结尾 返回 null
                fName
//                        判断是否 以 .class 皆为
                        .filter(name ->
//                                此处返回false 那么 filter 将会返回 null
                                name.endsWith(".class"))
                        .ifPresent(name -> {
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
