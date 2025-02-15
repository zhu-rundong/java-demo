package com.zrd.nio.chat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName ChatClient_1
 * @Description 聊天程序客户器端1。0版本
 * @Author ZRD
 * @Date 2025/2/15
 **/
public class ChatClient {
    //服务器地址
    private final String HOST = "127.0.0.1";
    //服务器端口
    private int PORT = 9999;
    //网络通道
    private SocketChannel socketChannel;
    //聊天用户名
    private String userName;

    public ChatClient() throws Exception{
        //创建SocketChannel
        socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //连接服务器
        InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("客户端连接需要时间，请耐心等待....");
            }
        }
        //得到客户端IP地址和端口信息，作为聊天用户名使用
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("---------------Client(" + userName + ") is ready---------------");
    }

    public void sendMessage(String message) throws Exception{
        if("bye".equals(message)){
            socketChannel.close();
            return;
        }
        message = userName + "说：" + message;
        try{
            ByteBuffer buffer= ByteBuffer.wrap(message.getBytes());
            socketChannel.write(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void readMessage() throws Exception{
        try{
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readLength = socketChannel.read(buffer);
            if(readLength > 0){
                String message = new String(buffer.array());
                System.out.println(message.trim());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
