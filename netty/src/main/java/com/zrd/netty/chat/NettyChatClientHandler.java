package com.zrd.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName NettyChatClientHandler
 * @Description 客户端业务处理类
 * @Author ZRD
 * @Date 2025/2/15
 **/
public class NettyChatClientHandler extends SimpleChannelInboundHandler<String> {
    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        System.out.println(s.trim());
    }
}
