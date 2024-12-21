package com.zrd.cahce;

import com.zrd.cahce.computable.Computable;
import com.zrd.cahce.computable.SimpleFunction;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @ClassName ComputeCache
 * @Description 计算缓存
 * @Author ZRD
 * @Date 2024/9/28
 **/
public class ComputeCache<A,V> implements Computable<A,V> {
    //使用ConcurrentHashMap保证并发安全
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private Computable<A,V> computable;

    public ComputeCache(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public V compute(A args) throws Exception {
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
        return future.get();
    }

    public static void main(String[] args) throws Exception {
        ComputeCache<String, Integer> computeCache = new ComputeCache<>(new SimpleFunction());
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
