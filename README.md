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