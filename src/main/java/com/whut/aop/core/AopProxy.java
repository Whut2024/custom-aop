package com.whut.aop.core;

import com.whut.aop.advice.Advice;
import com.whut.aop.chain.DefaultMethodInvocation;
import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author whut2024
 * @since 2024-09-17
 */
public class AopProxy {





    @AllArgsConstructor
    static class ChainDefinitionMethodInterceptor implements MethodInterceptor {

        private final ProxyFactory proxyFactory;

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
