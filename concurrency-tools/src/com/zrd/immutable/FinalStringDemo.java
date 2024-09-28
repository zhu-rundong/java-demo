package com.zrd.immutable;

/**
 * @ClassName FinalStringDemo
 * @Description TODO
 * @Author ZRD
 * @Date 2024/9/22
 **/
public class FinalStringDemo {
    public static void main(String[] args) {
        demo1();
    }
    public static void demo1(){
        String a = "s1";
        //b被final修饰，不可变，编译器在编译期间当作常量使用
        final String b = "s";
        String c = "s";
        //等同于 d = "s" + 1 ，直接计算出 a = "s1"，常量池中已经有对象a = "s1"，因此，d指向与 a 一样的地址
        String d = b + 1;
        // e的值在运行时才能确定，生成的值存储在堆上，最终，e指向的是堆上的s1
        String e = c + 1;
        System.out.println(a == d);
        System.out.println(a == e);
        //output: true  false
    }
}
