package com.zrd.lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName CinemaReadWrite
 * @Description 读写锁示例
 * @Author ZRD
 * @Date 2024/8/25
 **/
public class CinemaReadWrite {
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.ReadLock readLock =  reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock =  reentrantReadWriteLock.writeLock();

    private static void read(){
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"获取到了读锁，正在读取");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(Thread.currentThread().getName()+"释放读锁");
            readLock.unlock();
        }
    }

    private static void write(){
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"获取到了写锁，正在写入");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(Thread.currentThread().getName()+"释放写锁");
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(CinemaReadWrite::read,"Thread1").start();
        new Thread(CinemaReadWrite::read,"Thread2").start();
        new Thread(CinemaReadWrite::write,"Thread3").start();
        new Thread(CinemaReadWrite::write,"Thread4").start();
    }
}
