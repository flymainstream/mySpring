





DI:

getBean 
doGetBean
createBean
doCreateBean 
createBeanInstance --> 反射newBean --> 封装BeanWrapper

populateBean --> 从 BeanWrapper 中找到需要赋值的属性 
             --> 把需要赋值的属性封装成一个 PropertyValues , 其中每一个 PropertyValue 都是Bean中的属性需要的对象 

applyPropertyValues()  --> 循环 PropertyValue, 将其赋值到 BeanWrapper 所包含的 Bean中             

