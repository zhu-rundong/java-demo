package com.zrd.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName InterruptDemo
 * @Description 中断线程
 * @Author ZRD
 * @Date 2024/11/10
 **/
public class InterruptDemo {
    static volatile boolean isStop = false;
    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void main(String[] args) {
        //m1();
        //m2();
        m3();
    }

    public static void m3() {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("-----isInterrupted() = true，程序结束。");
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //线程的中断标志位为false，需要再次调用interrupt()设置true
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                System.out.println("------hello Interrupt");
            }
        }, "t1");
        t1.start();

        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        //修改t1线程的中断标志位为true
        new Thread(t1::interrupt,"t2").start();
    }

    /**
     * 通过AtomicBoolean
     */
    public static void m2() {
        new Thread(() -> {
            while(true) {
                if(atomicBoolean.get()) {
                    System.out.println("-----atomicBoolean.get() = true，程序结束。");
                    break;
                }
                System.out.println("------hello atomicBoolean");
            }
        },"t1").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> atomicBoolean.set(true),"t2").start();
    }

    /**
     * 通过volatile变量实现
     */
    public static void m1() {
        new Thread(() -> {
            while(true) {
                if(isStop) {
                    System.out.println("-----isStop = true，程序结束。");
                    break;
                }
                System.out.println("------hello isStop");
            }
        },"t1").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> isStop = true,"t2").start();
    }
}
