package com.zrd.jvm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @ClassName XlassFileClassLoader
 * @Description 自定义 Classloader，加载xlass/Hello.xlass 文件，执行 hello 方法
 * 此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件
 * @Author ZRD
 * @Date 2021/4/17
 **/
public class XlassFileClassLoader extends ClassLoader{

    public static void main(String[] args) {
        try {
            XlassFileClassLoader xlassFileClassLoader = new XlassFileClassLoader();
            Class<?> hello = xlassFileClassLoader.findClass("Hello");
            Object obj = hello.newInstance();
            Method method = hello.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        Class clazz = null;
        InputStream resourceAsStream = XlassFileClassLoader.class.getClassLoader()
                .getResourceAsStream("xlass/Hello.xlass");
        if (resourceAsStream != null) {
            try {
                //获取xlass文件的字节
                byte[] sourceByteArr = readAllBytes(resourceAsStream);
                byte[] targetByteArr = new byte[sourceByteArr.length];
                //处理字节，x = 255 - x
                for(int i = 0; i < sourceByteArr.length; i++){
                    targetByteArr[i] = (byte) (255 - sourceByteArr[i]);
                }
                clazz = defineClass(name, targetByteArr, 0, targetByteArr.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (clazz == null) {
            throw new ClassNotFoundException(name+"文件未找到！");
        }
        return clazz;
    }

    /**
     * @Description InputStream转byte数组
     * JDK 9+可以使用inputStream.readAllBytes()或使用Common-Io包中IOUtils.toByteArray(inputStream)
     * @param inputStream
     * @return byte[]
     * @author ZRD
     * @Date 2021/4/17
     */
    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400;
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;
        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1){
                    outputStream.write(buf, 0, readLen);
                }
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) {
                inputStream.close();
            } else{
                try {
                    inputStream.close();
                } catch (IOException e) {
                    exception.addSuppressed(e);
                }
            }
        }
    }
}
