package com.zrd.flowcontrol.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ConditionDemo1
 * @Description Condition基本用法
 * @Author ZRD
 * @Date 2024/9/22
 **/
/**
 Condition接口
 ◆ 控制线程的“等待”和“唤醒”
 ◆ 方法：
     ① await()：阻塞线程
     ② signal()：唤醒被阻塞的线程
     ③ signalAll()会唤起所有正在等待的线程。
     ◆ 注意：
     ① 调用await()方法时必须持有锁，否则会抛出异常
     ② Condition和Object#await/notify方法用法一样，两者await方法都会释放锁
 */
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
