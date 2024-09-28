package com.zrd.cahce.computable;

/**
 * @ClassName FailFunction
 * @Description 失败计算
 * @Author ZRD
 * @Date 2024/9/28
 **/
public class FailFunction implements Computable<String,Integer>{
    @Override
    public Integer compute(String args) throws Exception {
        double random = Math.random();
        if(random > 0.5){
            throw new Exception("计算失败，数值"+random+"大于0.5");
        }
        Thread.sleep(3000);
        return Integer.parseInt(args);
    }
}
