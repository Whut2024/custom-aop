package com.whut.aop.pointcut;

import java.lang.reflect.Method;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public interface Pointcut {

    boolean matcher(Method method, Class<?> targetClass);
}
