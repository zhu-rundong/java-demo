package com.zrd.atomic;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @ClassName LongAccumulatorDemo
 * @Description long类型的聚合器，需要传入一个long类型的二元操作，可以用来计算各种聚合操作，包括加减乘除
 * @Author ZRD
 * @Date 2024/11/30
 **/
public class LongAccumulatorDemo {
    public static void main(String[] args) {
        //LongAdder只能用来计算加法，且从零开始计算
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        System.out.println(longAdder.longValue());

        LongAccumulator longAccumulator = new LongAccumulator((x,y)->x * y,1);
        longAccumulator.accumulate(1);
        longAccumulator.accumulate(2);
        longAccumulator.accumulate(4);
        System.out.println(longAccumulator.longValue());
    }
}
