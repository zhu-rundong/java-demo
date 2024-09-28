package com.zrd.lock.locks;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName TryLockDeadLock
 * @Description 使用tryLock避免死锁
 * @Author ZRD
 * @Date 2024/7/21
 **/
public class TryLockDeadLock implements Runnable{
    static Lock lock1 = new ReentrantLock();
    static Lock lock2 = new ReentrantLock();
    int flag = 1;

    public static void main(String[] args) {
        TryLockDeadLock tryLockDeadLock1 = new TryLockDeadLock();
        TryLockDeadLock tryLockDeadLock2 = new TryLockDeadLock();
        tryLockDeadLock1.flag = 1;
        tryLockDeadLock2.flag = 2;
        new Thread(tryLockDeadLock1).start();
        new Thread(tryLockDeadLock2).start();
    }
    @Override
    public void run() {
        for(int i = 0 ; i < 100 ; i++){
            if(flag == 1){
                try {
                    //This usage ensures that the lock is unlocked if it was acquired, and doesn't try to unlock if the lock was not acquired.
                    if(lock1.tryLock() || lock1.tryLock(500, TimeUnit.MILLISECONDS)){
                        try{
                            System.out.println("线程1获取到了锁1");
                            Thread.sleep(new Random().nextInt(1000));
                            if(lock2.tryLock() || lock2.tryLock(500, TimeUnit.MILLISECONDS)){
                                try{
                                    System.out.println("线程1获取到了锁2");
                                    System.out.println("线程1成功获取两把锁");
                                    break;
                                }finally {
                                    lock2.unlock();
                                }
                            }else{
                                System.out.println("线程1获取锁2失败，已重试");
                            }
                        }finally {
                            lock1.unlock();
                            Thread.sleep(new Random().nextInt(1000));
                        }
                    }else{
                        System.out.println("线程1获取锁1失败，已重试");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(flag == 2){
                try {
                    //This usage ensures that the lock is unlocked if it was acquired, and doesn't try to unlock if the lock was not acquired.
                    if(lock2.tryLock() || lock2.tryLock(3000, TimeUnit.MILLISECONDS)){
                        try{
                            System.out.println("线程2获取到了锁2");
                            Thread.sleep(new Random().nextInt(1000));
                            if(lock1.tryLock() || lock1.tryLock(3000, TimeUnit.MILLISECONDS)){
                                try{
                                    System.out.println("线程2获取到了锁1");
                                    System.out.println("线程2成功获取两把锁");
                                    break;
                                }finally {
                                    lock1.unlock();
                                }
                            }else{
                                System.out.println("线程2获取锁1失败，已重试");
                            }
                        }finally {
                            lock2.unlock();
                            Thread.sleep(new Random().nextInt(1000));
                        }
                    }else{
                        System.out.println("线程2获取锁2失败，已重试");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
