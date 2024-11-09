package com.zrd.completablefuture;

import java.util.concurrent.*;

/**
 * @ClassName CompletableFutureDemo
 * @Description CompletableFuture 基础使用
 * @Author ZRD
 * @Date 2024/10/27
 **/
public class CompletableFutureBasicDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //基础用法
        //demo1();
        //阶段计算，一个阶段完成以后，触发另一个阶段
        demo2();
    }

    public static void demo1() throws ExecutionException, InterruptedException {
        //自定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,10,5L, TimeUnit.SECONDS,new LinkedBlockingDeque<>(50),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        //无返回值
        //没有指定线程池，使用默认的ForkJoinPool.commonPool()
        CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName()+"----------------------1");
        });
        System.out.println(voidCompletableFuture1.get());
        CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName()+"----------------------2");
        },threadPoolExecutor);
        System.out.println(voidCompletableFuture2.get());
        //有返回值
        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName()+"----------------------3");
            return 3;
        });
        System.out.println(completableFuture3.get());
        CompletableFuture<Integer> completableFuture4 = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName()+"----------------------4");
            return 4;
        },threadPoolExecutor);
        System.out.println(completableFuture4.get());
        threadPoolExecutor.shutdown();
    }

    public static void demo2() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(()->{
            try {
                //暂停一下，模拟计算过程
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        }).thenApply(pre->{
            return pre + 2;
        }).whenComplete((value,ex)->{
            if(ex == null){
                //无异常信息，计算成果
                System.out.println("compute finish-------------------"+value);
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        //效果等同于之前的Future，会阻塞
        //System.out.println(completableFuture.get());
        //System.out.println(completableFuture.join());
        //join()和get()对比：
        //join()和get()方法效果是一样的，区别是join()方法不会抛出异常
        System.out.println("---------main thread over---------");

        //output:---------main thread over---------
        //计算结果（compute finish-------------------3）未输出，由于主线程结束，CompletableFuture默认使用的线程池会立刻关闭，暂停2秒钟线程查看计算结果
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        //---------main thread over---------
        //compute finish-------------------3
    }
}
