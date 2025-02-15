package com.zrd.netty.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @ClassName NettyServerHandler
 * @Description 业务处理类ServerHandler
 * @Author ZRD
 * @Date 2025/2/15
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据事件，处理客户端发送过来的数据
     *
     * @param ctx   ChannelHandlerContext对象，包含通道的上下文信息
     * @param msg   客户端发送的数据对象
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //用缓冲区接受数据
        ByteBuf buf=(ByteBuf) msg;
        //转成字符串
        System.out.println("client msg："+buf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕事件，读取完客户端数据后回复客户端
     *
     * @param ctx	ChannelHandlerContext对象，包含通道的上下文信息
     */
    public void channelReadComplete(ChannelHandlerContext ctx){
        //Unpooled.copiedBuffer获取到缓冲区，第一个参数是向客户端传的字符串
        ctx.writeAndFlush(Unpooled.copiedBuffer("宝塔镇河妖",CharsetUtil.UTF_8));
    }

    //异常发生事件
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        //异常时关闭ctx，ctx是相关信息的汇总，关闭它其它的也就关闭了
        ctx.close();
    }
}
