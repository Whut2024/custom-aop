package com.whut.threadpool;

/**
 * @author liuqiao
 * @since 2024-11-21
 */
@FunctionalInterface
public interface PoolExceptionHandler {

    void handle(Throwable e);
}
