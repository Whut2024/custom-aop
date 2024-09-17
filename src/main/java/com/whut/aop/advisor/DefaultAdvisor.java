package com.whut.aop.advisor;

import com.whut.aop.advice.Advice;
import com.whut.aop.pointcut.Pointcut;
import com.whut.aop.pointcut.TruePointcut;
import lombok.Getter;

/**
 * 默认切面，封装切点和增强逻辑
 * @author whut2024
 * @since 2024-09-17
 */
@Getter
public class DefaultAdvisor implements Advisor {


    private final Pointcut pointcut;

    private final Advice advice;

    public DefaultAdvisor(Pointcut pointcut, Advice advice) {
        this.pointcut = pointcut;
        this.advice = advice;
    }

    public DefaultAdvisor(Advice advice) {
        this.pointcut = TruePointcut.getInstance();
        this.advice = advice;
    }
}
