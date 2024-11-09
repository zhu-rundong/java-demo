package com.zrd.thread;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName DaemonThread
 * @Description t1设置为守护线程，当main线程执行完毕，意味着程序需要完成的业务操作已经结束了
 * ，此时虽然守护线程t1还未执行结束，但是系统也自动退出了。所以当系统只剩下守护进程的时候，java虚拟机会自动退出。
 * @Author ZRD
 * @Date 2024/10/20
 **/
public class DaemonThread {
    public static void main(String[] args){
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 开始运行，"+(Thread.currentThread().isDaemon() ? "守护线程":"用户线程"));
            while (true) {

            }
        }, "t1");
        //线程的daemon属性为true表示是守护线程，false表示是用户线程
        t1.setDaemon(true);
        t1.start();
        //2秒钟后主线程再运行
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(Thread.currentThread().getName()+"----------task is over");
    }


}
