package com.zrd.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @ClassName AtomicIntegerFieldUpdaterDemo
 * @Description 以一种线程安全的方式操作非线程安全对象内的某些字段
 * @Author ZRD
 * @Date 2024/11/30
 **/
public class AtomicIntegerFieldUpdaterDemo {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();
        for(int i = 1 ; i <= 1000 ; i++){
            new Thread(()->{
                bankAccount.add(bankAccount);
            }).start();
        }
        try { Thread.sleep( 1000 ); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("bankAccount.money----------------"+bankAccount.money);
    }
}

class BankAccount{
    //更新的对象属性必须使用 public volatile 修饰符
    public volatile int money = 0;
    //因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性
    AtomicIntegerFieldUpdater<BankAccount> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class,"money");

    public void add(BankAccount bankAccount){
        atomicIntegerFieldUpdater.incrementAndGet(bankAccount);
    }
}
