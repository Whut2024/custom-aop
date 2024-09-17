package com.whut.aop.pointcut;

import java.lang.reflect.Method;

/**
 * always matching
 *
 * @author whut2024
 * @since 2024-09-17
 */
public class TruePointcut implements Pointcut {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return true;
    }

    private TruePointcut() {
    }


    private volatile static Pointcut pointcut;


    public static Pointcut getInstance() {
        if (pointcut == null) {
            synchronized (TruePointcut.class) {
                if (pointcut == null)
                    pointcut = new TruePointcut();
            }
        }

        return pointcut;
    }
}
