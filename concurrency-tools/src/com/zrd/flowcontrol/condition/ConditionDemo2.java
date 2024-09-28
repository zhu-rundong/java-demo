package com.zrd.flowcontrol.condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ConditionDemo2
 * @Description 使用Condition实现生产者消费者模式
 * @Author ZRD
 * @Date 2024/9/22
 **/
public class ConditionDemo2 {
    private final int queueSize = 10;
    private final PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public static void main(String[] args) {
        ConditionDemo2 conditionDemo2 = new ConditionDemo2();
        Producer producer = conditionDemo2.new Producer();
        Consumer consumer = conditionDemo2.new Consumer();
        producer.start();
        consumer.start();
    }


    class Consumer extends Thread{
        @Override
        public void run(){
            consume();
        }
        private void consume(){
            while (true){
                lock.lock();
                try {
                    while(queue.isEmpty()){
                        System.out.println("队列空，等待数据...");
                        notEmpty.await();
                    }
                    queue.poll();
                    notFull.signal();
                    System.out.println("从队列中取走一个数据，队列剩余"+queue.size()+"元素");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Producer extends Thread{
        @Override
        public void run(){
            producer();
        }
        private void producer(){
            while (true){
                lock.lock();
                try {
                    while(queue.size() == queueSize){
                        System.out.println("队列满，等待有空余...");
                        notFull.await();
                    }
                    queue.offer(1);
                    notEmpty.signal();
                    System.out.println("向队列中插入一个数据，队列剩余空间"+(queueSize-queue.size()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
