package com.zrd.flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName CountDownLatchDemo2
 * @Description 多个线程等待某一线程信号，同时开始执行
 * @Author ZRD
 * @Date 2024/9/22
 **/
public class CountDownLatchDemo2 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 1 ; i <= 5 ; i++){
            Runnable runnable = getRunnable(i, countDownLatch);
            executorService.submit(runnable);
        }
        Thread.sleep(5000);
        System.out.println("开始执行......");
        countDownLatch.countDown();
        executorService.shutdown();
    }

    private static Runnable getRunnable(int i, CountDownLatch countDownLatch) {
        return () -> {
            try {
                System.out.println("No."+ i +"准备完成......");
                countDownLatch.await();
                System.out.println("No."+ i +"开始执行......");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
