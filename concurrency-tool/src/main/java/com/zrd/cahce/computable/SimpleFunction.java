package com.zrd.cahce.computable;

/**
 * @ClassName SimpleFunction
 * @Description 简单计算
 * @Author ZRD
 * @Date 2024/9/28
 **/
public class SimpleFunction implements Computable<String,Integer>{
    @Override
    public Integer compute(String args) throws Exception {
        Thread.sleep(3000);
        return Integer.parseInt(args);
    }
}
