package com.demo.nio;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author eli
 * @date 2017/9/11 18:12
 */
public class TransferToClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",9026));
        socketChannel.configureBlocking(true);
        FileChannel fileChannel = new FileInputStream("D:/Test/file.txt").getChannel();
        //做好标记量
        long size = fileChannel.size();
        int pos = 0;
        int offset = 4096;
        long curnset = 0;
        long counts = 0;
        while (pos<size){
            curnset = fileChannel.transferTo(pos,4096,socketChannel);
            pos+=offset;
            counts+=curnset;
        }
        //关闭
        fileChannel.close();
        socketChannel.close();
        //打印传输字节数
        System.out.println(counts);




    }
}
