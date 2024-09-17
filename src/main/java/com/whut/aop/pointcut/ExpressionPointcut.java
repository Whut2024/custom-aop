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


    /**
     * 判断该方法是否匹配目标类
     *
     * @param method 待匹配的方法
     * @param targetClass 目标类
     * @return 如果匹配则返回true，否则返回false
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return true;
    }
}
