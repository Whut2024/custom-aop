package com.whut.aop.advice;

import com.whut.aop.chain.MethodInvocation;

/**
 * <p> enhancing logic is defined there  </p>
 * <p> we should cooperate with invocation chain </p>
 *
 * @author whut2024
 * @since 2024-09-17
 */
public interface Advice {

    /**
     * 调用方法执行
     *
     * @param methodInvocation 方法调用对象
     * @return 执行结果
     * @throws Throwable 抛出执行过程中可能遇到的异常
     */
    Object invoke(MethodInvocation methodInvocation) throws Throwable;

}
