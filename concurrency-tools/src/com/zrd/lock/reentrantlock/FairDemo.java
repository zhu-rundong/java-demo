package com.zrd.lock.reentrantlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName FairDemo
 * @Description 演示公平锁和非公平锁
 * @Author ZRD
 * @Date 2024/7/28
 **/
public class FairDemo {
    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread [] thread = new Thread[10];
        for (int i = 0; i < thread.length; i++) {
            thread[i] = new Thread(new job(printQueue));
        }
        for (int i = 0; i < thread.length; i++) {
            thread[i].start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class job implements Runnable{

    private PrintQueue printQueue;

    public job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "开始打印");
        printQueue.printJob(new Object());
        System.out.println(Thread.currentThread().getName() + "打印完毕");
    }
}

class PrintQueue{
    //参数true：公平锁，无参默认false
    private Lock lock = new ReentrantLock(true);

    public void printJob(Object o){
        //第一次打印
        lock.lock();
        try {
            int duration = new Random().nextInt(3) + 1;
            System.out.println(Thread.currentThread().getName() + "第一次打印正在打印，需要 " + duration+" 秒");
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        //第二次打印
        lock.lock();
        try {
            int duration = new Random().nextInt(5) + 1;
            System.out.println(Thread.currentThread().getName() + "第二次打印正在打印，需要 " + duration+" 秒");
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
