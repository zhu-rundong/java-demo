package com.zrd.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @ClassName ABADemo
 * @Description ABA问题
 * @Author ZRD
 * @Date 2024/11/21
 **/
public class ABADemo {
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<Integer>(100,1);
    public static void main(String[] args) {
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"第一次版本号："+ atomicStampedReference.getStamp());
            try { Thread.sleep( 1000 ); } catch (InterruptedException e) { e.printStackTrace(); }
            atomicStampedReference.compareAndSet(100,101,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"第二次版本号："+ atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101,100,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"第三次版本号："+ atomicStampedReference.getStamp());
        },"t1").start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"第一次版本号："+ stamp);
            try { Thread.sleep( 3000 ); } catch (InterruptedException e) { e.printStackTrace(); }
            boolean result = atomicStampedReference.compareAndSet(100,1024,stamp,stamp+1);
            System.out.println(Thread.currentThread().getName()+":"+result+":"+atomicStampedReference.getReference());
        },"t2").start();
    }
}
