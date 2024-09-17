package com.whut.aop.pointcut;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author whut2024
 * @since 2024-09-17
 */
@AllArgsConstructor
public class ExpressionPointcut implements Pointcut {


    private final String expression;




    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return true;
    }
}
