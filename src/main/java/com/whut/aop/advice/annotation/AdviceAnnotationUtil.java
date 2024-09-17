package com.whut.aop.advice.annotation;

import com.whut.aop.advice.*;
import com.whut.aop.advisor.DefaultAdvisor;
import com.whut.aop.pointcut.ExpressionPointcut;
import com.whut.aop.pointcut.Pointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析切面类的工具类
 * @author whut2024
 * @since 2024-09-17
 */
public class AdviceAnnotationUtil {


    /**
     * 根据目标类解析出所有的通知增强器（Advisor）
     *
     * @param targetClass 目标类
     * @return 所有通知增强器（Advisor）的列表
     * @throws RuntimeException 当实例化目标类时抛出异常时，将异常封装为RuntimeException并抛出
     */
    public static List<DefaultAdvisor> resolveAdvisorFromClass(Class<?> targetClass) {
        final Method[] declaredMethods = targetClass.getDeclaredMethods();
        final List<DefaultAdvisor> advisorList = new ArrayList<>(declaredMethods.length);
        final Object object;
        try {
            object = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Before.class)) {
                final Before before = method.getAnnotation(Before.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new BeforeAdvice(method, object)));
            } else if (method.isAnnotationPresent(Around.class)) {
                final Around before = method.getAnnotation(Around.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AroundAdvice(method, object)));

            } else if (method.isAnnotationPresent(After.class)) {
                final After before = method.getAnnotation(After.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AfterAdvice(method, object)));

            } else if (method.isAnnotationPresent(AfterThrowing.class)) {
                final AfterThrowing before = method.getAnnotation(AfterThrowing.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AfterThrowingAdvice(method, object)));

            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                final AfterReturning before = method.getAnnotation(AfterReturning.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AfterReturningAdvice(method, object)));

            }
        }

        return advisorList;
    }
}
