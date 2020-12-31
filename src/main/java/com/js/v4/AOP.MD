IOC 、 DI 、MVC 工作模式

核心类为 ApplicationContext ， 是整个容器的主要class
BeanDefinition 是Bean的定义
BeanWrapper 是Bean对象的增强
BeanDefinitionReader 是Bean 定义的读取者， 从文件 or 注解上读取 并解析
    
DI：
    getBean（） --》 
    instantiateBean（）； 反射创建对象 ，并封装成单例 
核心:populateBean() 时并赋值成员变量 依赖注入, 使用反射注入 
    问题: 
        循环注入: 使用缓存解决循环注入问题

MVC: 
核心:DispatchServlet -> init() 函数  
        初始化九大组件
        initStrategies()
            HandlerMapping
            HandlerAdapter
            ViewResolver
        service()
           返回 ModelAndView 对象
           View.render() 渲染 读取模板文件内容
            
            
           
AOP设计思想:
    AOP基于动态代理.
    
代理的功能:
    增强、解耦
    保护原有对象
JAVA中代理方式有两种：
    1. 实现同一个接口
    2. 继承被代理的类
    在被增强 类的前后织入代码，生成一个新的临时类 (Aspect)
    

流程:
    IOC - AOP - DI - MVC 
        
 
Spring 设计:
 getBean 函数触发 --> ApplicationContext   --> AdvisedSupport --> AopConfig --> Advice
 --> jdkDynamicAopProxy
