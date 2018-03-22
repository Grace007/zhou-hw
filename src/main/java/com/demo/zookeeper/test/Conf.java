package com.demo.zookeeper.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author eli
 * @date 2017/8/11 11:31
 */
public class Conf{
    public static void waitUntilConnected(ZooKeeper zooKeeper, CountDownLatch connectedLatch) {
        if (States.CONNECTING == zooKeeper.getState()) {
            try {
                connectedLatch.await();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    static class ConnectedWatcher implements Watcher {

        private CountDownLatch connectedLatch;

        ConnectedWatcher(CountDownLatch connectedLatch) {
            this.connectedLatch = connectedLatch;
        }

        @Override
        public void process(WatchedEvent event) {
            if (event.getState() == KeeperState.SyncConnected) {
                connectedLatch.countDown();
            }
        }
    }
    static public Conf Instance(){
        if(static_ == null){
            static_ = new Conf();
        }
        return static_;
    }
    public boolean Init(String hostports, int times){
        try{
            CountDownLatch connectedLatch = new CountDownLatch(1);
            Watcher watcher = new ConnectedWatcher(connectedLatch);
            zk_ = new ZooKeeper(hostports, times, watcher);
            waitUntilConnected(zk_, connectedLatch);
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }
    public String Get(String keys){
        String re = "";
        String ppath = "/servers";
        int oldpos = -1;
        int pos = 0;
        while(true){
            pos = keys.indexOf(".", oldpos + 1);
            if(pos < 0){
                ppath += "/";
                String str = keys.substring(oldpos + 1);
                ppath += str;
                break;
            }
            ppath += "/";
            String str = keys.substring(oldpos + 1,  pos);
            ppath += str;
            oldpos = pos;
        }
        Stat stat = new Stat();
        try{
            byte[] b = zk_.getData(ppath, false, stat);    //获取节点的信息及存储的数据
            re = new String(b);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return re;
    }
    private Conf(){

    }
    private static ZooKeeper zk_;

    public static ZooKeeper getZk_() {
        return zk_;
    }

    public static void setZk_(ZooKeeper zk_) {
        Conf.zk_ = zk_;
    }

    static private Conf static_;
    public static void main(String args[]) throws KeeperException, InterruptedException {
        String hostports = "192.168.50.11:2181,192.168.50.12:2181,192.168.50.13:2181";

        Conf.Instance().Init(hostports, 1000);

        String str = Conf.Instance().Get("server1");
        str = Conf.Instance().Get("server1");
        System.out.println(str+"===");

        ZooKeeper zooKeeper =Conf.getZk_();
        byte [] data = zooKeeper.getData("/servers/server1".toString(),false,null);

        System.out.println(new String(data));

        while(true){
            try{Thread.sleep(100);}
            catch(Exception e){

            }
        }

    }
}
