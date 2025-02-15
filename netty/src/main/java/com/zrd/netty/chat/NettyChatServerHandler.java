package com.zrd.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName NettyChatServerHandler
 * @Description 服务端业务处理类
 * @Author ZRD
 * @Date 2025/2/15
 **/
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {
    public static List<Channel> channels = new ArrayList<>();

    //通道就绪
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel inChannel=ctx.channel();
        channels.add(inChannel);
        System.out.println("[Server]:"+inChannel.remoteAddress().toString().substring(1)+"上线");
    }
    //通道未就绪
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel inChannel=ctx.channel();
        channels.remove(inChannel);
        System.out.println("[Server]:"+inChannel.remoteAddress().toString().substring(1)+"离线");
    }
    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        Channel inChannel=ctx.channel();
        System.out.println("s = " + s);
        for(Channel channel:channels){
            //广播
            if(channel!=inChannel){
                channel.writeAndFlush("["+inChannel.remoteAddress().toString().substring(1)+"]"+"说："+s+"\n");
            }
        }
    }
}
