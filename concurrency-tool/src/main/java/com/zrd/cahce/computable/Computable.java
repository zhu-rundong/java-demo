package com.zrd.cahce.computable;

/**
 * @InterfaceName Computable
 * @Description 计算接口
 * @Author ZRD
 * @Date 2024/9/28
 */
public interface Computable<A,V> {
    V compute(A args) throws Exception;
}
