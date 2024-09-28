package com.zrd.flowcontrol.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ConditionDemo1
 * @Description Condition基本用法
 * @Author ZRD
 * @Date 2024/9/22
 **/
public class ConditionDemo1 {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public static void main(String[] args) {
        ConditionDemo1 conditionDemo1 = new ConditionDemo1();
        new Thread(conditionDemo1::method2).start();
        conditionDemo1.method1();
    }
    void method1(){
        lock.lock();
        try {
            System.out.println("条件不满足，开始await");
            condition.await();
            System.out.println("条件满足，开始执行");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    void method2(){
        lock.lock();
        try {
            Thread.sleep(1000);
            System.out.println("准备工作完成，开始唤醒其他线程");
            condition.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }


}
