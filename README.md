# 自定义AOP



## 代理对象创建器

Enhancer



## 代理对象方法逻辑替换器

MethodInterceptor



## 切面

Pointcut

Advice MethodInterceptor

Advisor



MethodInvocation



## 整体流程

代理对象创建器创建代理

调用一个被增强的代理方法

方法中的逻辑替换器执行

创建一条包含符合资格切面的调用链然后执行



注意，这个 AOP 没有导入 SpringBoot 环境，没有对应的 AopAutoConfiguration 意味着没有相应的 AopProxyCreator (InstantiationBeanPostProcessor) ，无法完成代理类的自动创建，只完成了 beforeInstantiation -> wrapIfNessary -> createProxy -> 中创建 AopFactory 以后的大致部分