package com.zrd.flowcontrol.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @ClassName SemaphoreDemo
 * @Description Semaphore用法
 * @Author ZRD
 * @Date 2024/9/22
 **/
public class SemaphoreDemo {
    static Semaphore semaphore = new Semaphore(5,true);
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for(int i = 0 ; i < 100 ; i++){
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
                semaphore.acquire();
                //一次可以获取和释放多个许可证，获取和释放的数量必须一致（编程规范）
                //semaphore.acquire(5);
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
                //semaphore.release(5);
            }
        }
    }
}
