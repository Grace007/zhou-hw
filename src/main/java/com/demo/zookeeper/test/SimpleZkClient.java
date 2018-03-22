package com.demo.zookeeper.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * @author eli
 * @date 2017/8/10 11:53
 */
public class SimpleZkClient {
    //1.创建zookeeper实例
    private static final  String CONNECTURL="192.168.50.11:2181,192.168.50.12:2181,192.168.50.13:2181";
    private static final int TIMEOUT=50000;
    //private static CountDownLatch connectedSeemaphore = new CountDownLatch(1);
    public Watcher watcher=new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            // 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
            System.out.println(event.getType() + "---" + event.getPath());
            try {
                zooKeeper.getChildren("/", true);
            } catch (Exception e) {
            }

        }
    };
    public ZooKeeper zooKeeper;
    private void creatZkInstance() throws IOException{
        zooKeeper = new ZooKeeper(CONNECTURL,TIMEOUT,watcher);
    }

    private void close() throws InterruptedException {
        zooKeeper.close();
    }
    private void operations() throws KeeperException, InterruptedException {
        /*System.out.println("/n1. 创建 ZooKeeper 节点 (znode ： znode1, 数据： myData2 ，权限： OPEN_ACL_UNSAFE ，节点类型： Persistent");
        zooKeeper.create("/znode1","myData2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        System.out.println("znode1 = " + zooKeeper.getData("/znode1",false,null));
        System.out.println("znode1 = " + zooKeeper.setData("/znode1","大数据".getBytes(),-1));
        zooKeeper.delete("/znode1",-1);
        System.out.println("znode1 = " + zooKeeper.exists("/znode1",false));*/
        /*System.out.println("znode1 = " + zooKeeper.exists("/zookeeper/znode1",false));
        zooKeeper.create("/zookeeper/znode4","znode's data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);*/
        /*zooKeeper.create("/zookeeper/znode2/znode2-1","2-1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);*/
        List<String> children = zooKeeper.getChildren("/servers", true);
        for (String child : children) {
            System.out.println(child);
        }
        Thread.sleep(Long.MAX_VALUE);

    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        SimpleZkClient simpleZkClient = new SimpleZkClient();
        simpleZkClient.creatZkInstance();
        simpleZkClient.operations();
        simpleZkClient.close();

    }





}
