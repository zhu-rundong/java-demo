package com.zrd.completablefuture;

import java.util.concurrent.*;

/**
 * @ClassName CompletableFutureCommonDemo
 * @Description CompletableFuture 常用方法
 * @Author ZRD
 * @Date 2024/10/27
 **/
public class CompletableFutureCommonDemo {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,10,5L, TimeUnit.SECONDS,new LinkedBlockingDeque<>(50),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        try {
            //获得结果和触发计算
            //getAndComplete(threadPoolExecutor);
            //对计算结果进行处理
            //dealResult();
            //对计算结果进行消费
            //consumeResult();
            //对计算速度进行选用
            //applyToEither();
            //对计算结果进行合并
            //mergeResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolExecutor.shutdown();
        }
    }

    private static void mergeResult() throws Exception{
        //1.-------thenCombine()-------
        //两个CompletionStage任务都完成后，最终能把两个任务的结果一起交给thenCombine，先完成的先等着，等待其它分支任务
        CompletableFuture<Integer> thenCombineResult = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 1");
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 2");
            return 20;
        }), (x,y) -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 3");
            return x + y;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 4");
            return 30;
        }),(a,b) -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 5");
            return a + b;
        });
        System.out.println(thenCombineResult.get());

    }

    private static void applyToEither() throws Exception{
        //1.-------applyToEither()-------
        //那个任务执行速度快，使用那个任务的结果
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //暂停1秒
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            //暂停2秒
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }), r -> {
            return r;
        });
        System.out.println(integerCompletableFuture.join());

        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    private static void consumeResult() throws Exception{
        //1.-------thenAccept()-------
        //接收任务的处理结果，并消费处理，无返回结果
        CompletableFuture.supplyAsync(()->{
            System.out.println("---------------------------1");
            return 1;
        }).thenApply(f ->{
            System.out.println("---------------------------2");
            return f + 2;
        }).thenApply(f ->{
            System.out.println("---------------------------3");
            return f + 3;
        }).thenAccept(System.out::println);
        //------------------------任务之间的顺序执行-----------------------------
        //任务 A 执行完执行 B，并且 B 不需要 A 的结果
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());
        //任务 A 执行完执行 B，B 需要 A 的结果，但是任务 B 无返回值
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {}).join());
        //任务 A 执行完执行 B，B 需要 A 的结果，同时任务 B 有返回值
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB").join());

    }
    private static void dealResult() throws Exception{
        //1.-------thenApply()-------
        //计算结果存在依赖关系，线程串行执行
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println("---------------------------1");
            return 1;
        }).thenApply(f ->{
            System.out.println("---------------------------2");
            //出现异常情况，那步出错就停在那步，相当于try/catch
            int age = 10/0;
            return f + 2;
        }).thenApply(f ->{
            System.out.println("---------------------------3");
            return f + 3;
        }).whenComplete((v,e)->{
            if(e == null){
                System.out.println("-----------result:"+v);
            }
        }).exceptionally(ex->{
            ex.printStackTrace();
            return null;
        });
        System.out.println(completableFuture.get());

        //2.-------handle()-------
        //计算结果存在依赖关系，线程串行执行
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(()->{
            System.out.println("---------------------------1");
            return 1;
        }).handle((f,e) ->{
            System.out.println("---------------------------2");
            //出现异常情况，也可以往下一步走，根据带的异常参数可以进一步处理，相当于try/finally
            int age = 10/0;
            return f + 2;
        }).handle((f,e) ->{
            System.out.println("---------------------------3");
            return f + 3;
        }).whenComplete((v,e)->{
            if(e == null){
                System.out.println("-----------result:"+v);
            }
        }).exceptionally(ex->{
            ex.printStackTrace();
            return null;
        });
        System.out.println(completableFuture.get());

        //3.-------thenCompose()-------
        //计算结果存在依赖关系，线程串行执行
        //thenApply（）转换的是泛型中的类型，是同一个CompletableFuture
        //thenCompose（）用来连接两个CompletableFuture，是生成一个新的CompletableFuture
        CompletableFuture<Integer> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 1");
            return 10;
        }).thenCompose(i -> CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "\t" + "---come in 2");
                return i * 10;
            }));
        System.out.println(stringCompletableFuture.get());

        //----方法带后缀Async后缀区别，例如
        // thenApply：执行当前任务的线程继续执行thenApply的任务
        // thenApplyAsync：将执行thenApply任务提交给线程池来执行

    }


    private static void getAndComplete(ThreadPoolExecutor threadPoolExecutor) throws Exception {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
            //暂停2秒
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            return 1;
        },threadPoolExecutor);
        /*
         *******************获取结果***********************
         */
        //1.-------completableFuture.get()-------
        //返回结果，立即阻塞
        //System.out.println(completableFuture.get());

        //2.-------completableFuture.get(long timeout, TimeUnit unit)-------
        //等待1秒，未返回结果，抛出TimeoutException
        System.out.println(completableFuture.get(1L,TimeUnit.SECONDS));
        //output：Exception in thread "main" java.util.concurrent.TimeoutException

        //3.-------completableFuture.getNow(T valueIfAbsent)-------
        //任务完成，返回结果；未完成，返回设定的valueIfAbsent值
        //try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        //System.out.println(completableFuture.getNow(99));
        //output：1
        //try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        //System.out.println(completableFuture.getNow(99));
        //output：99

        /*
         *******************主动触发计算***********************
         */
        //-------completableFuture.complete(T value)-------
        //如果任务尚未完成，返回设定的值
        //try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        //System.out.println(completableFuture.complete(-99)+"======"+completableFuture.get());
        //output：true======-99
        //如果任务完成，返回任务结果
        //try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        //System.out.println(completableFuture.complete(-99)+"======"+completableFuture.get());
        //output：false======1
    }
}
