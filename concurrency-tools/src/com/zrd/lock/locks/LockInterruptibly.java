package com.zrd.lock.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockInterruptibly
 * @Description TODO
 * @Author ZRD
 * @Date 2024/7/21
 **/
public class LockInterruptibly implements Runnable{
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        LockInterruptibly lockInterruptibly = new LockInterruptibly();

        Thread thread0 = new Thread(lockInterruptibly);
        Thread thread1 = new Thread(lockInterruptibly);
        thread0.start();
        thread1.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread0.interrupt();
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"尝试获取锁");
        try {
            lock.lockInterruptibly();
            try{
                System.out.println(Thread.currentThread().getName()+"获取到了锁");
                Thread.sleep(5000);
            }catch (InterruptedException e){
                System.out.println(Thread.currentThread().getName()+"睡眠期间被中断");
            }finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName()+"释放了锁");
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+"获取锁期间被中断");
        }
    }
}
