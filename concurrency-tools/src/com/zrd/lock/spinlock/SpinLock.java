package com.zrd.lock.spinlock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName SpinLock
 * @Description 自旋锁
 * @Author ZRD
 * @Date 2024/8/25
 **/
public class SpinLock {
    private static AtomicReference<Thread> sign = new AtomicReference<>();

    private void lock(){
        Thread currentThread = Thread.currentThread();
        while (!sign.compareAndSet(null,currentThread)){
            System.out.println(currentThread.getName()+"自旋获取失败，再次尝试自旋获取");
        }
    }

    private void unlock(){
        Thread currentThread = Thread.currentThread();
        sign.compareAndSet(currentThread,null);
    }
 
    public static void main(String[] args) {
        SpinLock spinLock = new SpinLock();
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName()+"开始尝试获取自旋锁");
            spinLock.lock();
            System.out.println(Thread.currentThread().getName()+"获取到了自旋锁");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(Thread.currentThread().getName()+"释放了自旋锁");
                spinLock.unlock();
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
