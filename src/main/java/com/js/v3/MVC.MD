MVC设计思想:

1. MultipartResolver  多文件上传组件
2. LocaleResolver 本地语言环境
3. ThemeResolver  主体模板处理器
4. HandlerMapping 保存Url 映射关系
5. HandlerAdapter 动态参数适配器
6. HandlerExceptionResolver 异常拦截器
7. RequestToViewNameTranslator 视图解析器,从request中获取 viewName
8. ViewResolvers 视图转换器 模板引擎
9. FlashMapManager 参数缓存器



流程  HandlerMapping --> HandlerAdapter --> ModelAndView --> ViewResolver --> view 

HandlerMapping 保存url 与函数的映射

HandlerAdapter 拿到 函数的形参列表 , 然后赋值

ModelAndView 对返回值的一个封装 (不属于9大组件)

ViewResolver 根据ModelAndView 进行判断 是 model还是View 


