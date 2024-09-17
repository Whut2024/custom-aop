package com.whut.aop.advice;

import com.whut.aop.chain.MethodInvocation;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author whut2024
 * @since 2024-09-17
 */
@AllArgsConstructor
public class AroundAdvice implements MethodInterceptor {

    private final Method method;

    private final Object object;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        method.invoke(object);
        return methodInvocation.proceed();
    }
}
