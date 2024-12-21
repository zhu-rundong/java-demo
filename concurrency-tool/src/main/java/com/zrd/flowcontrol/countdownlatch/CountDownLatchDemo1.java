package com.zrd.flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName CountDownLatchDemo1
 * @Description 一个线程等待多个线程执行完毕，再继续执行
 * @Author ZRD
 * @Date 2024/9/22
 **/
public class CountDownLatchDemo1 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 1 ; i <= 5 ; i++){
            Runnable runnable = getRunnable(i, countDownLatch);
            executorService.submit(runnable);
        }
        System.out.println("等待5个线程执行......");
        countDownLatch.await();
        System.out.println("5个线程执行完毕");
        executorService.shutdown();

    }

    private static Runnable getRunnable(int i, CountDownLatch countDownLatch) {
        return () -> {
            try {
                Thread.sleep((long)(Math.random()*10000));
                System.out.println("No."+ i +"执行完成");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                countDownLatch.countDown();
            }
        };
    }
}
