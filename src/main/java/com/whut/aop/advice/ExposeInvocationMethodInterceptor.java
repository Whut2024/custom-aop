package com.whut.aop.advice;

import com.whut.aop.chain.MethodInvocation;

/**
 * 将调用链对象存入ThreadLocal
 * @author whut2024
 * @since 2024-09-17
 */
public class ExposeInvocationMethodInterceptor implements MethodInterceptor {


    private final static ThreadLocal<MethodInvocation> invocation = new ThreadLocal<>();

    private ExposeInvocationMethodInterceptor() {
    }

    public static final ExposeInvocationMethodInterceptor INSTANCE = new ExposeInvocationMethodInterceptor();


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            invocation.set(methodInvocation);
            return methodInvocation.proceed();
        } finally {
            invocation.remove();
        }
    }
}
