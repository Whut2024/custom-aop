package com.whut.aop.core;

import com.whut.aop.advice.Advice;
import com.whut.aop.chain.DefaultMethodInvocation;
import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public class AopProxy {


    @AllArgsConstructor
    static class ChainDefinitionMethodInterceptor implements MethodInterceptor {

        private final ProxyFactory proxyFactory;

        /**
         * 代理方法的增强逻辑在此方法中形成调用链并执行
         *
         * @param proxy 被代理对象
         * @param method 被代理的方法
         * @param args 被代理方法的参数
         * @param methodProxy 方法代理对象
         * @return 执行结果
         * @throws Throwable 抛出执行过程中可能遇到的异常
         */
        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            // get original class information
            final Object target = proxyFactory.getTarget();
            final Class<?> targetClass = target.getClass();

            // get eligible advice
            final List<Advice> adviceList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

            // check whether we should create a chain
            if (adviceList.size() == 1) {
                // just contains a expose invocation advice
                return methodProxy.invoke(target, args);
            }

            // build a chain
            return (new DefaultMethodInvocation(target, args, methodProxy, adviceList)).proceed();
        }
    }
}
