package com.zrd.lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName RecursionDemo
 * @Description 演示可重入锁
 * @Author ZRD
 * @Date 2024/7/28
 **/
public class RecursionDemo {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        dealData();
    }

    private static void dealData(){
        lock.lock();
        try{
            System.out.println("数据处理完成");
            if(lock.getHoldCount() < 5){
                System.out.println("----------"+lock.getHoldCount());
                dealData();
                System.out.println("**********"+lock.getHoldCount());
            }
        }finally {
            lock.unlock();
        }
    }
}
