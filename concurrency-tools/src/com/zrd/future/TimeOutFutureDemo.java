package com.zrd.future;

import java.util.concurrent.*;

/**
 * @ClassName TimeOutFutureDemo
 * @Description get超时方法
 * @Author ZRD
 * @Date 2024/9/24
 **/
public class TimeOutFutureDemo {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    static class Ad {

        String name;

        public Ad(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static class FetchAdTask implements Callable<Ad> {
        @Override
        public Ad call() throws Exception {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("sleep期间被中断了");
                return new Ad("被中断时候的默认广告");
            }
            return new Ad("这是一条广告");
        }
    }

    public void printAd() {
        Future<Ad> f = executorService.submit(new FetchAdTask());
        Ad ad;
        try {
            ad = f.get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            ad = new Ad("被中断时候的默认广告");
        } catch (ExecutionException e) {
            ad = new Ad("异常时候的默认广告");
        } catch (TimeoutException e) {
            ad = new Ad("超时时候的默认广告");
            System.out.println("超时，未获取到广告");
            boolean cancel = f.cancel(true);
            System.out.println("cancel的结果：" + cancel);
        }
        executorService.shutdown();
        System.out.println(ad);
    }

    public static void main(String[] args) {
        new TimeOutFutureDemo().printAd();
    }
}
