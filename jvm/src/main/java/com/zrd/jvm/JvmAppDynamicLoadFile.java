package com.zrd.jvm;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @ClassName JvmAppClassLoaderAddURL
 * @Description 动态加载 class 文件
 * @Author ZRD
 * @Date 2021/4/18
 **/
public class JvmAppDynamicLoadFile {
    public static void main(String[] args) {
        //将Hello.java生成的Hello.class文件放入D:/class文件夹下
        String path = "file:/D:/class/";
        classLoaderBeforeJDK9(path);
        //classLoaderAfterJDK9(path);
    }
    /**
     * @Description JDK9 之前
     * @param path
     * @return void
     * @author ZRD
     * @Date 2021/4/18
     */
    public static void classLoaderBeforeJDK9(String path){
        URLClassLoader classLoader = (URLClassLoader) JvmAppDynamicLoadFile.class.getClassLoader();
        try {
            //JDK9 以后无效，因为应用类加载器和扩展类加载的父类不再是 URLClassLoader
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            URL url = new URL(path);
            addURL.invoke(classLoader,url);
            Class.forName("com.zrd.jvm.Hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @Description JDK9 之后
     * @param path	
     * @return void
     * @author ZRD
     * @Date 2021/4/18
     */
    public static void classLoaderAfterJDK9(String path){
        try {
            URL url = new URL(path);
            URL[] urls = new URL[]{url};
            Class.forName("com.zrd.jvm.Hello", true, new URLClassLoader(urls));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
