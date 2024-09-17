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

    Object invoke(MethodInvocation methodInvocation) throws Throwable;

}
