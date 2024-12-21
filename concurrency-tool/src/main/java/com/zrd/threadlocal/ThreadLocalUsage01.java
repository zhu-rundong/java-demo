package com.zrd.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName ThreadLocalUsage01
 * @Description 利用ThreadLocal，给每个线程分配自己的dateFormat对象，保证了线程安全，高效利用内存
 * @Author ZRD
 * @Date 2024/3/24
 **/
public class ThreadLocalUsage01 {
    //线程池
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);
    //SimpleDateFormat 是线程不安全的类，一般不要定义为static变量
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static void main(String[] args) {
        for(int i = 0 ; i < 20 ; i++){
            int finalI = i;
            //executorService.submit(() -> System.out.println(unSafeDate(finalI)));
            executorService.submit(() -> System.out.println(new ThreadLocalUsage01().date(finalI)));
        }
        executorService.shutdown();
    }


    public static String unSafeDate(int seconds){
        Date date = new Date(1000L * seconds);
        return simpleDateFormat.format(date);
    }

    public String date(int seconds){
        //单位毫秒，从1970.1.1 00:00:00 GMT开始计时
        String format;
        try {
            Date date = new Date(1000L * seconds);
            format = ThreadSafeDateFormatter.dateFormatThreadLocal.get().format(date);
        } finally {
            ThreadSafeDateFormatter.dateFormatThreadLocal.remove();
        }
        return format;
    }

}

class ThreadSafeDateFormatter{
    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(()-> new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
}
