package com.whut.aop.chain;

/**
 * a basic invocation chain for advisor invoking interface
 *
 * @author whut2024
 * @since 2024-09-17
 */
public interface MethodInvocation {


    Object proceed() throws Throwable;
}
