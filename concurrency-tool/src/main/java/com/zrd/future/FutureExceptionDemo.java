package com.zrd.future;

import java.util.concurrent.*;

/**
 * @ClassName FutureExceptionDemo
 * @Description TODO
 * @Author ZRD
 * @Date 2024/9/24
 **/
public class FutureExceptionDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Integer> submit = executorService.submit(new CallableTask());
        try {
            for(int i = 0 ; i < 5 ;i++){
                System.out.println(i);
                Thread.sleep(300);
            }
            //output: true，判断任务是否执行完毕（执行完毕不代表执行成功）
            System.out.println(submit.isDone());
            //只有在执行get方法，才会抛出异常
            submit.get();
        } catch (InterruptedException e) {
            System.out.println("抛出InterruptedException异常");
        }catch (ExecutionException e) {
            System.out.println("抛出ExecutionException异常");
        }
        executorService.shutdown();
    }

    static class CallableTask implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            throw new IllegalAccessException();
        }
    }
}
