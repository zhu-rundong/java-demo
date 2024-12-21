package com.zrd.atomic;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @ClassName AtomicMarkAbleReferenceDemo
 * @Description 原子更新带有标记位的引用类型对象，它的定义就是将状态戳简化为true或false，解决是否修改过问题
 * @Author ZRD
 * @Date 2024/11/25
 **/
public class AtomicMarkAbleReferenceDemo {
        static AtomicMarkableReference<Integer> atomicMarkableReference = new AtomicMarkableReference<>(100,false);

    public static void main(String[] args) {
        new Thread(()->{
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName()+"默认修改标识："+ marked);
            try { Thread.sleep( 1000 ); } catch (InterruptedException e) { e.printStackTrace(); }
            atomicMarkableReference.compareAndSet(100,101,marked,!marked);
        },"t1").start();

        new Thread(()->{
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName()+"默认修改标识："+ marked);
            try { Thread.sleep( 2000 ); } catch (InterruptedException e) { e.printStackTrace(); }
            boolean b = atomicMarkableReference.compareAndSet(100, 103, marked, !marked);
            System.out.println(Thread.currentThread().getName()+"是否修改成功："+ b);
            System.out.println(Thread.currentThread().getName()+"修改标识："+ atomicMarkableReference.isMarked()+"，当前值："+atomicMarkableReference.getReference());
        },"t2").start();
    }
}
