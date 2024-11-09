package com.zrd.future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @ClassName OneFutureDemo
 * @Description TODO
 * @Author ZRD
 * @Date 2024/9/24
 **/
public class OneFutureDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Integer> submit = executorService.submit(new CallableTask());
        try {
            //一旦调用get()方法，无论任务是否执行完毕都会导致阻塞
            System.out.println(submit.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
    }

    static class CallableTask implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            Thread.sleep(2000);
            return new Random().nextInt();
        }
    }
}
