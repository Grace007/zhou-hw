package com.demo.zookeeper.zkDist;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper.States;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author eli
 * @date 2017/8/10 17:13
 */
public class ZookeeperServer {
    private static final Logger logger = Logger.getLogger(ZookeeperServer.class);
    private static final String CONNECTURL="192.168.50.11:2181,192.168.50.12:2181,192.168.50.13:2181";
    private static final int TIMEOUT=50000;
    private static final String parentNode = "/servers";
    private static ZooKeeper zk = null;
    private static CountDownLatch countDownLatch =new CountDownLatch(1);


    public void getConnect() throws IOException {
        logger.info("zookeeper实例化开始...");
        zk = new ZooKeeper(CONNECTURL, TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
                if (event.getState() == KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
                try {
                    zk.getChildren("/", true);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        });
        if (States.CONNECTING == zk.getState()) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void registerServer(String hostName) throws KeeperException, InterruptedException {
        System.out.println("register方法开始执行");
        zk.create(parentNode+"/server",hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("hostName:" + hostName+" registerServer()执行,上线...");
    }
    public void handle(String hostName) throws InterruptedException {
        System.out.println("hostName = " + hostName+" handle()方法执行...");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZookeeperServer zookeeperServer = new ZookeeperServer();
        zookeeperServer.getConnect();
        zookeeperServer.registerServer("初始data");
        zookeeperServer.handle("初始data");

    }


}
