package com.zrd.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @ClassName AtomicReferenceFieldUpdaterDemo
 * @Description 多线程并发调用一个类的初始化方法，如果未被初始化过，将执行初始化工作，要求只能初始化一次
 * @Author ZRD
 * @Date 2024/11/30
 **/
public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) {
        MyInitClass myInitClass = new MyInitClass();
        for(int i = 1 ; i <= 5 ; i++){
            new Thread(()->{
                myInitClass.init(myInitClass);
            }).start();
        }
    }
}

class MyInitClass{
    public volatile Boolean isInit = Boolean.FALSE;
    AtomicReferenceFieldUpdater<MyInitClass,Boolean> atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MyInitClass.class,Boolean.class,"isInit");
    public void init(MyInitClass myInitClass) {
        if(atomicReferenceFieldUpdater.compareAndSet(myInitClass,Boolean.FALSE,Boolean.TRUE)) {
            System.out.println(Thread.currentThread().getName()+"---init.....start");
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"---init.....ent");
        }else{
            System.out.println(Thread.currentThread().getName()+"------其它线程正在初始化");
        }
    }
}
