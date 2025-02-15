package com.zrd.nio.chat.version1;

import java.util.Scanner;

/**
 * @ClassName TestChatVersion1
 * @Description 启动聊天程序客户端
 * @Author ZRD
 * @Date 2025/2/15
 **/
public class TestChat {
    public static void main(String[] args) throws Exception {
        ChatClient chatClient = new ChatClient();
        new Thread(()->{
            while (true){
                try {
                    chatClient.readMessage();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            chatClient.sendMessage(message);
        }
    }
}
