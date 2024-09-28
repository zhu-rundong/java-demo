package com.zrd.future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @ClassName FutureTaskDemo
 * @Description TODO
 * @Author ZRD
 * @Date 2024/9/24
 **/
public class FutureTaskDemo {
    public static void main(String[] args) {
        FutureTask<Integer> integerFutureTask = new FutureTask<>(new Task());
        //new Thread(integerFutureTask).start();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.submit(integerFutureTask);
        try {
            System.out.println(integerFutureTask.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
    }

    static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("子线程正在计算");
            Thread.sleep(2000);
            return new Random().nextInt();
        }
    }
}
