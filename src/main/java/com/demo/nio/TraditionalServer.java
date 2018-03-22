package com.demo.nio;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 1) 先将文件内容从磁盘中拷贝到操作系统buffer
 * 2) 再从操作系统buffer拷贝到程序应用buffer
 * 3) 从程序buffer拷贝到socket buffer
 * 4) 从socket buffer拷贝到协议引擎.
 *
 * 思路整理:
 * 1.建立ServerSocket实例,并且监听2000端口
 * 2.开始持续监测,建立循环
 * 2-1.通过ServerSocket.accept()建立Socket服务
 * 2-2.建立DataInputStream实例作为Socket的输入流
 * 2-3.建立byte[]
 * 2-4.从DataInputStream中循环读取字节流.当dateInputStream.read=-1的时候是循环出口
 * 2-5.关闭Socket连接
 *
 * @author eli
 * @date 2017/9/11 17:15
 */
public class TraditionalServer {
    public static void main(String[] args) throws Exception {
        // sas
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("serverSocket = " +serverSocket.getLocalSocketAddress());
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("socket = " + socket.getLocalSocketAddress());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            byte[] bytes = new byte[4096];
            while(true){
                int nread = inputStream.read(bytes,0,4096);
                if ((nread == -1)){
                    break;
                }
                System.out.println("接受的信息:"+nread);
                System.out.println(new String(bytes,"UTF-8"));

            }
            socket.close();
        }

    }
}
