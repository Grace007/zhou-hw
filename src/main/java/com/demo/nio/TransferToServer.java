package com.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *
 * 1) 先将文件内容从磁盘中拷贝到操作系统buffer
 * 2) 再从操作系统buffer通过transferTo()直接传输到Socketbuffer
 * 4) 从socket buffer拷贝到协议引擎.
 *
 * 思路整理:NIO技术直接从发送端的byteBuffer发送到接收端的byteBuffer,省去了程序应用buffer的开销,相当于通道技术.
 * @author eli
 * @date 2017/9/11 18:11
 */
public class TransferToServer {
    public static void main(String[] args) throws Exception {
        //针对面向流的侦听套接字的可选择通道。
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        /**
         * 启用/禁用 SO_REUSEADDR 套接字选项。
         *关闭 TCP 连接时，该连接可能在关闭后的一段时间内保持超时状态（通常称为 TIME_WAIT 状态或 2MSL 等待状态）。对于使用已知套接字地址或端口的应用程序而言，如果存在处于超时状态的连接（包括地址和端口），可能不能将套接字绑定到所需的 SocketAddress。
         *在使用 bind(SocketAddress) 绑定套接字前启用 SO_REUSEADDR 可允许上一个连接处于超时状态时绑定套接字。
         */
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress("localhost",9026));
        System.out.println("监听的ipPort:"+serverSocket.getLocalSocketAddress());
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while(true){
            // 接收数据
            SocketChannel channel = serverSocketChannel.accept();
            System.out.println("Accepted : "+channel);
            // 设置阻塞，接不到就停
            channel.configureBlocking(true);
            int nread = 0;
            while (nread !=-1){
                try{
                    nread = channel.read(byteBuffer);

                    byte[] bytes = byteBuffer.array();
                    System.out.println(new String(bytes,0,byteBuffer.position()));
                    byteBuffer.clear();
                }catch (IOException e){
                    e.printStackTrace();
                    nread = -1;
                }

            }



        }


    }
}
