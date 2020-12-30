package com.js.v3.framework.web.mvc.servlet;

import com.js.v1.MyController;
import com.js.v1.MyRequestMapping;
import com.js.v2.framework.context.MyApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author name
 * @date 2020/12/9
 * @dateTime 20:43
 * @description:
 */

public class DispatchServlet extends HttpServlet {
    private MyApplicationContext applicationContext;


    private Map<String, Object> handlerMapping = new HashMap<>(24);
    private List<MyHandlerMapping> handlerMappings = new ArrayList<>(24);

    private Map<MyHandlerMapping, MyHandlerAdapter> handlerAdapterMap = new HashMap<>(24);

    private List<MyViewResolver> viewResolvers = new ArrayList<>();

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

    private void dispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {


        /*1. 获得一个handlerMapping*/
        MyHandlerMapping handler = getMyHandlerMapping(req, resp);

        if (handler == null) {
            try {
//                resp.getWriter().write(" 404 Not Found !!! ");
                processDispatchResult(req, resp, new MyModelAndView("404"));
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*2. 获得一个handlerAdapter*/
        MyHandlerAdapter myHandlerAdapter = getMyHandlerAdapter(handler);

        /*3. 解析某个函数的形参和返回值之后 封装MyModeAndView返回*/
        MyModelAndView modelAndView = myHandlerAdapter.handler(req, resp, handler);
        /*4. 把 modelAndView 变成 ViewResolve*/
        processDispatchResult(req, resp, modelAndView);
    }

    private MyHandlerAdapter getMyHandlerAdapter(MyHandlerMapping handlerMapping) {
        if (handlerAdapterMap.isEmpty()) {
            return null;
        }
        return handlerAdapterMap.get(handlerMapping);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, MyModelAndView myModelAndView) throws IOException {
        if (myModelAndView == null) {
            resp.getOutputStream().write("123".getBytes());
        }
        if (this.viewResolvers.isEmpty()) {
            resp.getOutputStream().write("123".getBytes());
        }
        this.viewResolvers.forEach(element -> {

            MyView view = element.getView(myModelAndView.getViewName());
            view.render(myModelAndView.getModel(), req, resp);


            return;
        });

    }

    private MyHandlerMapping getMyHandlerMapping(HttpServletRequest req, HttpServletResponse resp) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = getUrl(req);
        for (MyHandlerMapping element : handlerMappings) {
            Matcher matcher = element.getUrl().matcher(url);
            if (matcher.matches()) {
                return element;

            }
        }
        return null;
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

//        初始化九大组件
        initStrategies(applicationContext);
        System.out.println(" finish init My Spring ");
    }

    private void initStrategies(MyApplicationContext context) {
//        初始化多文件上传组件
//        initMultipartResolver(context);
//        初始化本地语言环境
//        initLocaleResolver(context);
//        初始化模板处理器
//        initThemeResolver(context);
//        初始化 url映射器 必备
        initHandlerMapping(context);
//        初始化 参数适配器 必备
        initHandlerAdapters(context);
//        初始化异常拦截器
//        initHandlerExceptionResolvers(context);
//        初始化视图预处理器
//        initRequestViewNameTranslator(context);
//        初始化视图转换器
        initViewResolvers(context);
//        初始化FlashMap 管理器
        initFlashMapManager(context);

    }

    private void initFlashMapManager(MyApplicationContext context) {
    }

    private void initViewResolvers(MyApplicationContext context) {
        String templateRoot = (String) context.getConfig().getProperty("templateRoot");

        String templateRootFilePath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootFile = new File(templateRootFilePath);
        for (File file : templateRootFile.listFiles()) {
            viewResolvers.add(new MyViewResolver(file.getPath()));

        }
    }

    private void initRequestViewNameTranslator(MyApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(MyApplicationContext context) {
    }

    private void initHandlerAdapters(MyApplicationContext context) {
        handlerMappings.forEach(element -> {
            handlerAdapterMap.put(element, new MyHandlerAdapter());
        });

    }

    private void initHandlerMapping(MyApplicationContext context) {
        if (applicationContext.getBeanDefinitionCounts() == 0) {
            return;
        }
        applicationContext.getBeanDefinitionNames().forEach(this::doHandlerMapping);


    }

    private void initThemeResolver(MyApplicationContext context) {
    }

    private void initLocaleResolver(MyApplicationContext context) {


    }

    private void initMultipartResolver(MyApplicationContext context) {

    }


    private void doHandlerMapping(String beanName) {
        Class<?> aClass = applicationContext.getBean(beanName).getClass();
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
            String regex = (classUrl + "/" + methodUrl).replace("\\*", ".*").replaceAll("/+", "/");

            handlerMappings.add(new MyHandlerMapping(Pattern.compile(regex), method, applicationContext.getBean(beanName)));
        }


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
