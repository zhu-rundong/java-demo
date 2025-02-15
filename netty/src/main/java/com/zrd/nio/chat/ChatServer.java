package com.zrd.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @ClassName ChatServer
 * @Description 聊天程序服务器端1。0版本
 * @Author ZRD
 * @Date 2025/2/9
 **/
public class ChatServer {
    //监听通道
    private ServerSocketChannel serverSocketChannel;
    //选择器
    private Selector selector;
    //服务器端口
    private static final int port = 9999;

    public ChatServer() {
        try {
            // 1. 开启Socket监听通道
            serverSocketChannel = ServerSocketChannel.open();
            // 2. 开启选择器
            selector = Selector.open();
            // 3.绑定端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            // 4. 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 5. 将选择器注册到监听通道并监听accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            printInfo("真人网络聊天室 启动.......");
            printInfo("真人网络聊天室 初始化端口 9999.......");
            printInfo("真人网络聊天室 初始化网络ip地址 127.0.0.1.......");
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void start() {
        try {
            while (true) {
                if (selector.select(2000) == 0){
                    printInfo("没有连接.....");
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel sc = serverSocketChannel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        printInfo(sc.getRemoteAddress() + "上线了");
                    }
                    if(key.isReadable()){
                        //读取客户端消息并广播
                        readMessage(key);
                    }
                    //删除当前处理过的SelectionKey，以免重复处理
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readMessage(SelectionKey key) {
        SocketChannel sc = null;
        try {
            sc = (SocketChannel) key.channel();
            //读取数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = 0;
            while ((len = sc.read(buffer)) > 0) {
                String msg = new String(buffer.array(), 0, len);
                printInfo(msg);
                //广播消息
                broadcast(sc,msg);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void broadcast(SocketChannel channel,String msg) {
        System.out.println("服务器广播消息了...");
        try {
            for (SelectionKey key : selector.keys()) {
                Channel targetChannel = key.channel();
                if (targetChannel instanceof SocketChannel && channel != targetChannel) {
                    SocketChannel destChannel = (SocketChannel) targetChannel;
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    destChannel.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printInfo(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
