package com.whut.aop.advice.annotation;

import com.whut.aop.advice.*;
import com.whut.aop.advisor.Advisor;
import com.whut.aop.advisor.DefaultAdvisor;
import com.whut.aop.pointcut.ExpressionPointcut;
import com.whut.aop.pointcut.Pointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public class AdviceAnnotationUtil {


    public static List<Advisor> resolveAdvisorFromClass(Class<?> targetClass) {
        final Method[] declaredMethods = targetClass.getDeclaredMethods();
        final List<Advisor> advisorList = new ArrayList<>(declaredMethods.length);
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
            } else if (method.isAnnotationPresent(After.class)) {
                final After before = method.getAnnotation(After.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AfterAdvice(method, object)));

            } else if (method.isAnnotationPresent(Around.class)) {
                final Around before = method.getAnnotation(Around.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AroundAdvice(method, object)));

            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                final AfterReturning before = method.getAnnotation(AfterReturning.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AfterReturningAdvice(method, object)));

            } else if (method.isAnnotationPresent(AfterThrowing.class)) {
                final AfterThrowing before = method.getAnnotation(AfterThrowing.class);
                final String expression = before.value();
                final Pointcut pointcut = new ExpressionPointcut(expression);

                advisorList.add(new DefaultAdvisor(pointcut, new AfterThrowingAdvice(method, object)));

            }
        }

        return advisorList;
    }
}
