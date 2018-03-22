package com.demo.hdfs.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * @author eli
 * @date 2017/8/17 15:44
 *
 *
 * 客户端去操作hdfs时，是有一个用户身份的
 * 默认情况下，hdfs客户端api会从jvm中获取一个参数来作为自己的用户身份：-DHADOOP_USER_NAME=hadoop
 *
 * 也可以在构造客户端fs对象时，通过参数传递进去
 *
 */
public class HdfsClientDemo {
    FileSystem fs=null;
    Configuration conf=null;
    @Before
    public void init() throws Exception{
        conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://mini1:9000");
        fs = FileSystem.get(new URI("hdfs://mini1:9000"),conf,"root");
    }
    @Test
    public void mkdir() throws Exception {
        boolean mkdirs = fs.mkdirs(new Path("/hdfsTest/mkdirTest"));
        System.out.println("mkdirs = " + mkdirs);
        fs.close();
    }
    @Test
    public void download() throws Exception{
        Thread.sleep(2000);
        fs.copyToLocalFile(false,new Path("/Test"),new Path("D:/Test"),true);
        fs.close();
    }
    @Test
    public void upload() throws Exception{
        Thread.sleep(2000);
        fs.copyFromLocalFile(new Path("D:/123.txt"),new Path("/"));
        fs.close();
    }
    @Test
    public void listTest() throws Exception{
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            System.err.println(fileStatus.getPath()+"================="+fileStatus.toString());
        }
        //会递归找到所有的文件
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while(listFiles.hasNext()){
            LocatedFileStatus next = listFiles.next();
            String name = next.getPath().getName();
            Path path = next.getPath();
            System.out.println(name + "---" + path.toString());
        }
    }
}
