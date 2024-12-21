package com.zrd.lock.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockSupportDemo
 * @Description 线程的等待和唤醒
 * @Author ZRD
 * @Date 2024/11/12
 **/
public class LockSupportDemo {

    static final Object objectLock = new Object();
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        //Object和Condition限制条件：线程先要获得并持有锁，必须在锁块(synchronized或lock)中；线程必须要先等待后唤醒，线程才能够被唤醒
        //syncWaitNotify();
        //lockAwaitSignal();
        //LockSupport:无锁块要求；Object和Condition的先唤醒后等待，LockSupport也支持
        //lockSupport();
        lockSupportMore();
    }
    public static void lockSupportMore(){
        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + " come in");
            LockSupport.park();
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + " 被唤醒");
        }, "a");
        a.start();

        new Thread(() -> {
            LockSupport.unpark(a);
            System.out.println(Thread.currentThread().getName()+"\t"+" 发出通知");
        },"b").start();
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            LockSupport.unpark(a);
            System.out.println(Thread.currentThread().getName()+"\t"+" 发出通知");
        },"c").start();
    }
    public static void lockSupport(){
        Thread a = new Thread(() -> {
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + " come in");
            LockSupport.park();
            //LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + " 被唤醒");
        }, "a");
        a.start();

        new Thread(() -> {
            LockSupport.unpark(a);
            //permit的上限是1，第二个LockSupport.park()被阻塞
            //LockSupport.unpark(a);
            System.out.println(Thread.currentThread().getName()+"\t"+" 发出通知");
        },"b").start();
    }

    public static void lockAwaitSignal() {
        new Thread(() -> {
            //暂停几秒钟线程
            //try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t"+" come in");
                condition.await();
                System.out.println(Thread.currentThread().getName()+"\t"+" 被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"a").start();

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName()+"\t"+" 发出通知");
            } finally {
                lock.unlock();
            }
        },"b").start();
    }

    public static void syncWaitNotify() {
        new Thread(() -> {
            //暂停几秒钟线程
            //try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName()+"\t"+"come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t"+"被唤醒");
            }
        },"a").start();


        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName()+"\t"+"发出通知");
            }
        },"b").start();
    }
}
