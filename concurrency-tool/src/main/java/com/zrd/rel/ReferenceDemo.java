package com.zrd.rel;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ReferenceDemo
 * @Description JVM四种引用类型
 * @Author ZRD
 * @Date 2024/12/14
 **/
public class ReferenceDemo {

    public static void main(String[] args) {
        //strongReference();
        //softReference();
        //weakReference();
        phantomReference();
    }

    public static void strongReference(){
        //创建一个对象并建立强引用
        MyObject myObject = new MyObject();
        System.out.println("-----------gc before:"+myObject);
        //手动开启GC
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("-----------gc after:"+myObject);
        //将强引用赋值为 null
        //在系统内存充足时，垃圾回收器不会立即回收对象，只有在需要释放内存时，垃圾回收器才会回收不再被引用的对象
        myObject = null;
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("-----------gc final:"+myObject);
    }

    public static void softReference(){
        //设置运行内存大小：-Xms10m -Xmx10m，当内存不够用的时候，soft会被回收
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("-----gc after内存够用: "+softReference.get());
        try {
            //new 一个大对象
            byte[] bytes = new byte[10 * 1024 * 1024];
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("-----gc after内存不够: "+softReference.get());
        }
    }

    public static void weakReference(){
        WeakReference<MyObject> weakReference = new WeakReference<>(new MyObject());
        System.out.println("-----gc before内存够用: "+weakReference.get());
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("-----gc after内存够用: "+weakReference.get());
    }

    public static void phantomReference(){
        MyObject myObject = new MyObject();
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();
        PhantomReference<MyObject> phantomRef = new PhantomReference<>(myObject, referenceQueue);
        // 对象实例不能通过虚引用直接获取
        System.out.println("myObject: " + phantomRef.get());
        System.gc();
        System.out.println(referenceQueue.poll());
    }
}


class MyObject{
    @Override
    protected void finalize() throws Throwable {
        System.out.println(Thread.currentThread().getName()+"---finalize method invoked....");
    }
}
