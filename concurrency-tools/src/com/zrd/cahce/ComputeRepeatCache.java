package com.zrd.cahce;

import com.zrd.cahce.computable.Computable;
import com.zrd.cahce.computable.FailFunction;
import com.zrd.cahce.computable.SimpleFunction;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName ComputeCache
 * @Description 计算失败后异常处理及重试
 * @Author ZRD
 * @Date 2024/9/28
 **/
public class ComputeRepeatCache<A,V> implements Computable<A,V> {
    //使用ConcurrentHashMap保证并发安全
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private Computable<A,V> computable;

    public ComputeRepeatCache(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public V compute(A args) throws Exception {
        while (true) {
            System.out.println("进入缓存机制");
            //Future解决重复计算问题
            Future<V> future = cache.get(args);
            if(future == null){
                Callable<V> callable = () -> computable.compute(args);
                FutureTask<V> ft = new FutureTask<>(callable);
                future = cache.putIfAbsent(args, ft);
                if (future == null) {
                    future = ft;
                    System.out.println("从FutureTask调用了计算函数");
                    ft.run();
                }
            }
            try {
                return future.get();
            } catch (CancellationException e) {
                //任务取消，移除缓存
                System.out.println("任务被取消了");
                cache.remove(args);
                throw e;
            } catch (InterruptedException e) {
                System.out.println("任务被中断了");
                //任务中断，移除缓存
                cache.remove(args);
                throw e;
            } catch (ExecutionException e) {
                System.out.println("计算错误，需要重试，错误信息："+e.getMessage());
                cache.remove(args);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ComputeRepeatCache<String, Integer> computeCache = new ComputeRepeatCache<>(new FailFunction());
        new Thread(() -> {
            try {
                Integer compute = computeCache.compute("123");
                System.out.println("第一次计算结果:"+compute);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                Integer compute = computeCache.compute("123");
                System.out.println("第二次计算结果:"+compute);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                Integer compute = computeCache.compute("124");
                System.out.println("第三次计算结果:"+compute);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
