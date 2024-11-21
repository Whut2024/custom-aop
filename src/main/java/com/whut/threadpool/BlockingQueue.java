package com.whut.threadpool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuqiao
 * @since 2024-11-21
 */
public class BlockingQueue<E> {

    public static void main(String[] args) {
        final BlockingQueue<Object> queue = new BlockingQueue<>(5);

        final Thread t1 = new Thread(()-> {
            for (int i = 0; i < 6; i++) {
                queue.put(i);
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < 6; i++) {
                queue.put(i);
            }

        }, "t1");

        final Thread t2 = new Thread(()-> {
            for (int i = 0; i < 6; i++) {
                System.out.println(queue.take());
            }

            for (int i = 0; i < 9; i++) {
                System.out.println(queue.poll(2000, TimeUnit.MILLISECONDS));
            }
        }, "t2");

        t1.start();
        t2.start();


    }

    private final ReentrantLock lock;

    private final Condition emptyWaitSet;

    private final Condition fullWaitSet;

    private final int capacity;

    private final Deque<E> queue;


    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        lock = new ReentrantLock();
        emptyWaitSet = lock.newCondition();
        fullWaitSet = lock.newCondition();
        queue = new ArrayDeque<>();
    }


    public E take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            final E e = queue.removeFirst();
            fullWaitSet.signal();
            return e;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }

    }


    public E poll(long waitTime, TimeUnit timeUnit) {
        long leastTime = timeUnit.toMillis(waitTime);
        final long start = System.currentTimeMillis();
        try {
            if (!lock.tryLock(waitTime, timeUnit))
                return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            leastTime -= (start - System.currentTimeMillis());
            leastTime *= 1000000;

            while (queue.isEmpty()) {
                try {
                    leastTime = emptyWaitSet.awaitNanos(leastTime);
                    if (leastTime <= 0)
                        return null;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            final E e = queue.removeFirst();
            fullWaitSet.signal();
            return e;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }
    }


    public void put(E element) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.addLast(element);
            emptyWaitSet.signal();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }


    public boolean isFull() {
        lock.lock();
        try {
            return capacity == queue.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }
}
