package com.zrd.flowcontrol.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @ClassName SemaphoreDemo
 * @Description Semaphore用法（）
 * @Author ZRD
 * @Date 2024/9/22
 **/
/**
 Semaphore信号量：
 ◆ 限制和管理数量有限的资源的使用
 ◆ 场景：Hystrix、Sentinel限流
 ◆ 方法：
     ① new Semaphore ((int permits) 可以创建公平的非公平的策略
     ② acquire()：获取许可证，获取许可证，要么获取成功，信号量减1，要么阻塞等待唤醒
     ③ release()：释放许可证，信号量加1，然后唤醒等待的线程
 */
public class SemaphoreDemo {

    static Semaphore semaphore = new Semaphore(3,true);
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 0 ; i < 10 ; i++){
            executorService.submit(new Task(i));
        }
        executorService.shutdown();
    }

    static class Task implements Runnable{
        public int no;
        public Task(int i) {
            this.no = i;
        }

        @Override
        public void run() {
            try {
                //一次可以获取和释放多个许可证，获取和释放的数量必须一致（编程规范）
                semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                System.out.println("No."+no+"拿到了许可证");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("No."+no+"释放了许可证");
                semaphore.release();
            }
        }
    }
}
