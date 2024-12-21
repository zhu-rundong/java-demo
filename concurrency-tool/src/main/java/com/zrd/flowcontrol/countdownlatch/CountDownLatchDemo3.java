package com.zrd.flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName CountDownLatchDemo2
 * @Description 多个线程等待某一线程信号，同时开始执行，当所有线程都执行完毕，再结束
 * @Author ZRD
 * @Date 2024/9/22
 **/
public class CountDownLatchDemo3 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 1 ; i <= 5 ; i++){
            Runnable runnable = getRunnable(i, begin,end);
            executorService.submit(runnable);
        }
        Thread.sleep(5000);
        System.out.println("开始执行......");
        begin.countDown();
        end.await();
        System.out.println("所有线程均执行完毕......");
        executorService.shutdown();
    }

    private static Runnable getRunnable(int i, CountDownLatch begin, CountDownLatch end) {
        return () -> {
            try {
                System.out.println("No."+ i +"准备完成......");
                begin.await();
                System.out.println("No."+ i +"开始执行......");
                Thread.sleep((long)(Math.random()*10000));
                System.out.println("No."+ i +"执行完毕......");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                end.countDown();
            }
        };
    }
}
