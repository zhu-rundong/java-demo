package com.zrd.jvm;

/**
 * @ClassName FinalStringDemo
 * @Description 字符串比较
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
        System.out.println(a == e.intern());
        //output: true  false true

        /*
        1、单独使用””引号创建的字符串都是常量，编译期就已经确定存储到String Pool（字符串常量池）中。
        2、使用new String(“”)创建的对象会存储到heap中，是运行期新创建的。
        3、使用只包含常量的字符串连接符，如”aa”+”bb”创建的也是常量，编译期就能确定已经存储到String Pool中。
        4、被final修饰的变量会变为常量，编译期就能确定已经存储到String Pool中。
        5、使用包含变量的字符串连接，如”a”+s创建的对象是运行期才创建的，存储到heap中。
        6、运行期调用String的intern()方法可以向String Pool中动态添加对象
        */
    }
}
