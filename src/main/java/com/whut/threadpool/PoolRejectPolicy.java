package com.whut.threadpool;

/**
 * @author liuqiao
 * @since 2024-11-21
 */
@FunctionalInterface
public interface PoolRejectPolicy<E> {

    void reject(BlockingQueue<E> queue, Runnable task);
}
