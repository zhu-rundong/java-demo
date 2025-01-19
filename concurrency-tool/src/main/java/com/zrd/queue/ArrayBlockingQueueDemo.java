package com.zrd.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ArrayBlockingQueueDemo
 * @Description 有10个面试者，只有1个面试官，大厅有3个位子让面试者休息，每个人面试时间5秒，模拟所有人面试的场景。
 * @Author ZRD
 * @Date 2025/1/19
 **/
public class ArrayBlockingQueueDemo {
    private static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
    public static void main(String[] args) {
        Engineer engineer = new Engineer(queue);
        Interviewer interviewer = new Interviewer(queue);
        new Thread(engineer).start();
        new Thread(interviewer).start();
    }

}

class Engineer implements Runnable{
    ArrayBlockingQueue<String> queue;

    public Engineer(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        String message = "";
        for (int i = 1; i <= 10; i++){
            message = "面试者" + i;
            try {
                queue.put(message);
                System.out.println(message + "等待面试...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            queue.put("stop");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Interviewer implements Runnable{
    ArrayBlockingQueue<String> queue;

    public Interviewer(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        System.out.println("面试官准备好了...");
        String message = "";
        try {
            while (!"stop".equals(message = queue.take())){
                System.out.println(message + "开始面试...");
                TimeUnit.SECONDS.sleep(5);
                System.out.println(message + "面试结束...");
            }
            System.out.println("面试官结束面试...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
