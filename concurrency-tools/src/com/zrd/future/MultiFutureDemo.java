package com.zrd.future;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @ClassName MultiFutureDemo
 * @Description 批量提交任务
 * @Author ZRD
 * @Date 2024/9/24
 **/
public class MultiFutureDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ArrayList<Future<Integer>> futures = new ArrayList<>();
        for(int i = 0 ; i < 20 ;i++){
            Future<Integer> submit = executorService.submit(new CallableTask());
            futures.add(submit);
        }
        for (Future<Integer> submit : futures) {
            try {
                System.out.println(submit.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
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
