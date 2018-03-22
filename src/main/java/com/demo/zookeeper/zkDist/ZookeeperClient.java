package com.demo.zookeeper.zkDist;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author eli
 * @date 2017/8/10 17:13
 */
public class ZookeeperClient {
    private static final  String CONNECTURL="192.168.50.11:2181,192.168.50.12:2181,192.168.50.13:2181";
    private static final int TIMEOUT=60000;
    private static final String parentNode = "/servers";
    private ZooKeeper zk = null;
    // 注意:加volatile的意义何在？
    //很像上锁一样,serverList会在堆内存上锁,不会被创建副本.是的每个线程得到的serverList的值一样的.
    //不加volatile时,serverList会被每一个线程得到并创建本线程的副本,可能使得每一个线程得到的值不一样.
    private volatile List<String> serverList;
    private static CountDownLatch countDownLatch =new CountDownLatch(1);
    /**
     * 创建到zk的客户端连接
     *
     * @throws Exception
     */
    public void getConnect() throws Exception {

        zk = new ZooKeeper(CONNECTURL, TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    //每调用一次这个方法，在构造函数中初始化的count值就减1
                    countDownLatch.countDown();
                }
                // 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
                try {
                    //重新更新服务器列表，并且注册了监听
                    getServerList();
                    System.out.println(serverList);
                } catch (Exception e) {
                }
            }
        });
        if (ZooKeeper.States.CONNECTING == zk.getState()) {
            try {
                //count的值等于0，然后主线程就能通过await()方法，恢复执行自己的任务
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

    }

    /**
     * 获取服务器信息列表
     *
     * @throws Exception
     */
    public void getServerList() throws Exception {

        // 获取服务器子节点信息，并且对父节点进行监听
        List<String> children = zk.getChildren(parentNode, true);

        // 先创建一个局部的list来存服务器信息
        List<String> servers = new ArrayList<String>();
        for (String child : children) {
            // child只是子节点的节点名
            byte[] data = zk.getData(parentNode + "/" + child, false, null);
            servers.add(new String(data));
        }
        // 把servers赋值给成员变量serverList，已提供给各业务线程使用
        serverList = servers;

        //打印服务器列表
        System.out.println(serverList);
        Thread.sleep(2000);

    }

    /**
     * 业务功能
     *
     * @throws InterruptedException
     */
    public void handleBussiness() throws InterruptedException {
        System.out.println("client start working.....");
        Thread.sleep(Long.MAX_VALUE);
    }




    public static void main(String[] args) throws Exception {

        // 获取zk连接
        ZookeeperClient client = new ZookeeperClient();
        client.getConnect();
        // 获取servers的子节点信息（并监听），从中获取服务器信息列表
        client.getServerList();

        // 业务线程启动
        client.handleBussiness();

    }


}
