package com.whut.threadpool;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuqiao
 * @since 2024-11-21
 */
@SuppressWarnings("all")
public class ThreadPool {

    public static void main(String[] args) {
        final ThreadPool pool = new ThreadPool(4, 6, 3000, new BlockingQueue<>(2), (queue, task) -> {
        }, (e) -> {
        });
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("add over");

    }

    // 任务线程
    private final class Worker extends Thread {

        private Runnable task;

        private final long waitTime;

        public Worker(Runnable task, long waitTime) {
            this.task = task;
            this.waitTime = waitTime;
        }

        @Override
        public void run() {
            // 任务为 NULL 时尝试从阻塞队列获取任务
            // 核心线程会一直阻塞等待任务的进入阻塞队列 非核心线程会超时等待
            while (task != null || (task = queue.poll(waitTime, TimeUnit.MILLISECONDS)) != null) {
                try {
                    task.run();
                } catch (Throwable e) { // 异常处理
                    exceptionHandler.handle(e);
                } finally {
                    task = null;
                }
            }

            threadNumbersLock.lock();
            threadNumber--;
            threadNumbersLock.unlock();

            System.out.println(Thread.currentThread().getName() + " 结束");
        }
    }

    private final BlockingQueue<Runnable> queue;

    private final PoolRejectPolicy<Runnable> rejectPolicy;

    private final PoolExceptionHandler exceptionHandler;

    private int coreSize;

    private int maxSize;

    private long expire;

    private final ReentrantLock threadNumbersLock;

    private volatile int threadNumber;


    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    // 初始化线程池
    public ThreadPool(int coreSize, int maxSize, long expire, BlockingQueue<Runnable> queue, PoolRejectPolicy<Runnable> rejectPolicy, PoolExceptionHandler exceptionHandler) {
        this.queue = queue;
        this.threadNumbersLock = new ReentrantLock();
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.expire = expire;
        this.rejectPolicy = rejectPolicy;
        this.exceptionHandler = exceptionHandler;
    }

    // 添加任务
    public void execute(Runnable task) {
        threadNumbersLock.lock();
        try {
            if (threadNumber < coreSize) {
                // 线程数小于核心线程数
                final Worker worker = new Worker(task, Long.MAX_VALUE / 1000001);
                threadNumber++;
                worker.start();
                return;
            } else if (!queue.isFull()) {
                // 阻塞队列未满
                queue.put(task);
                return;
            } else if (threadNumber < maxSize) {
                // 线程数小于最大线程数
                final Worker worker = new Worker(task, expire);
                threadNumber++;
                worker.start();
                return;
            }
        } finally {
            threadNumbersLock.unlock();

        }

        // 拒绝策略
        rejectPolicy.reject(queue, task);
    }
}
