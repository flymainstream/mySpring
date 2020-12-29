

Spring IoC 
DispatchServlet -->  BeanDefinitionReader --> ApplicationContext



从 servlet 到 application 

0. 调用servlet 的 init方法  (如果将这一步替换掉,下面的步骤不变也可以满足Spring 功能)
1. 加载配置文件
2. 扫描相关的类
3. 初始化Ioc容器
4. 进行AOP 切面
5. 进行DI复制
6. 开始 handlerMapping 映射url

IoC

AOP

DI

MVC


begin at applicationContext ............

what is ApplicationContext
    a class like 'bean' factory . this class has a method is getBean()    
    简单理解为 工厂类 , 其中一个函数为 getBean()
In the Spring class-beans is single on default case
在Spring 中的类默认是单例的
But In the Spring DI is after Init() method
但是DI是在初始化之后发生的 

So in the Spring  DI is getBean() calling 
所以 在spring中 DI 是由 getBean() 触发的

1. 调用getBean时创建对象
2. 调用getBean之后立即创建对象
3. 调用servlet init() 函数的时候就要初始化 ApplicationContext


ApplicationContext 只负责创建Bean 
BeanDefinitionReader 负责把 Bean 从配置文件中读取出来然后 变成一个BeanDefinition


三个类  
init  负责初始化  ApplicationContext , BeanDefinitionReader
BeanDefinitionReader 负责把 Bean 从配置文件中读取出来然后 变成一个BeanDefinition
ApplicationContext 只负责创建Bean 



模拟 Spring:

