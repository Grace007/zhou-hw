package com.demo.nio;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 *思路整理:
 * 1.建立Socket连接,绑定到指定的ip和端口
 * 2.建立DileInputStream实例读取本地本地文件做输入流
 * 3.建立DataOutputStream实例作为Socket的输出流
 * 4.建立byte[],读取指针read(-1为字节流没有数据,4096为字节流数据满了),读取总数total(数据总量)
 * 5.循环向DataOutputStream中写入byte[]. DateOutputStream.write
 * 6.关闭输出流,Socket,输入流
 * @author eli
 * @date 2017/9/11 17:14
 */
public class TraditionalClient {
    public static void main(String[] args) throws  Exception {
        long startTime=System.currentTimeMillis();
        Socket socket = new Socket("localhost",2000);
        System.out.println("socket = " + socket.getInetAddress() +":"+socket.getPort());
        FileInputStream inputStream = new FileInputStream("D:/Test/file.txt");
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        byte[] bytes = new byte[4096];
        long read = 0,total=0;
        while((read = inputStream.read(bytes))>0){
            total = total +read;
            System.out.println("total = " + total);
            outputStream.write(bytes);

        }

        outputStream.close();
        socket.close();
        inputStream.close();




        long endTime=System.currentTimeMillis();
        System.out.println("totalTime="+(endTime-startTime));



    }

}
