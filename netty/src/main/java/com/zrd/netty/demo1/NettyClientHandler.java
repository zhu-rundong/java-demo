package com.zrd.netty.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @ClassName NettyClientHandler
 * @Description 业务处理类ClientHandler
 * @Author ZRD
 * @Date 2025/2/15
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //通道就绪事件
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("天王盖地虎",CharsetUtil.UTF_8));
    }

    //读取数据事件
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("server msg："+buf.toString(CharsetUtil.UTF_8));
    }
}
