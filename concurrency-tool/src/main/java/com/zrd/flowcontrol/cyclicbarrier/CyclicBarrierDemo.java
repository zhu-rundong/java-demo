package com.zrd.flowcontrol.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName CyclicBarrierDemo
 * @Description TODO
 * @Author ZRD
 * @Date 2024/9/23
 **/
/*
    CyclicBarrier循环栅栏：
    ◆ 线程会等待，直到线程到了事先规定的数目，然后触发执行条件进行下一步动作
    ◆ 场景：并行计算
    ◆ 方法：
        ① new CyclicBarrier(int parties, Runnable barrierAction)参数1集结线程数，参数2凑齐之后执行的任务
        ② await()：阻塞当前线程，待凑齐线程数量之后继续执行
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("所有线程已经准备完成，可以执行");
            }
        });
        for (int i = 1 ; i <= 10; i++){
            new Thread(new Task(i,cyclicBarrier)).start();
        }
    }

    static class Task implements Runnable{
        private int no;
        private CyclicBarrier cyclicBarrier;

        public Task(int no, CyclicBarrier cyclicBarrier) {
            this.no = no;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                System.out.println("线程NO."+no+"正在准备中");
                Thread.sleep((long)(Math.random()*3000));
                System.out.println("线程NO."+no+"准备完成");
                cyclicBarrier.await();
                System.out.println("线程NO."+no+"开始执行");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
