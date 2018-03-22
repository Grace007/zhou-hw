package com.demo.newbase;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author eli
 * @date 2017/9/8 14:16
 */
public class ZKClient implements Watcher {
    private static Logger logger = Logger.getLogger(ZKClient.class);
    //一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
    private static CountDownLatch latch = new CountDownLatch(1);
    //Zookeeper会为每个Znode维护一个叫作Stat的数据结构
    private static Stat stat = new Stat();
    private static ZooKeeper zk = null;
    private static final String zookepper_Nodes="192.168.50.11:2181";
    private final static Integer SESSION_TIMEOUT = 60000;// 120000;
    // 节点名
    private static String node = "";
    // 节点所在分组
    private static String group = "";
    // 节点数据(节点配置信息)
    private static String config = "";
    private boolean lockWork = false;
    public static ZKClient zkClient;
    //单例模式
    public  synchronized ZKClient getZKClient(){
        if (zkClient == null){
            zkClient = new ZKClient();
        }
        return zkClient;
    }

    //程序入口
    public void doStart(String node1) throws  Exception{
        ZKClient.node=node1;

        String path="/servers/"+node;
        //ZooKeeper(String connectString, int sessionTimeout, Watcher watcher) throws IOException
        /**
         *connectString. zookeeper server列表, 以逗号隔开,  连接url列表字符串
         *sessionTimeout. 指定连接的超时时间.
         *watcher. 事件回调接口.
         */
        zk=new ZooKeeper(zookepper_Nodes,SESSION_TIMEOUT,new ZKClient());
        System.out.println("latch堵塞");

        latch.await();
        System.out.println("zk连接成功");
        System.out.println("zkClient node = " + node);
        System.out.println(path + " = " +getData(path));
        //System.out.println("设置"+path+"节点的数据:"+setData(path,"server1-1's data"));
        System.out.println("节点"+path+"状态:"+zk.exists(path,false));

        System.out.println("list = " + getChildren(path));

    }



    public String getData(String node1){
        try{
            byte[] temp = zk.getData(node1,true,stat);
            String data= new String (temp);
            return data;
        }catch (Exception e){
        }

        return null;
    }
    public boolean setData(String node,String nodedata){
        try{
            zk.setData(node,nodedata.getBytes(),-1);
            if (StringUtils.equals( new String(zk.getData(node,false,null)),nodedata)){
                //System.out.println(node + " = " +getData(node));
                return true;
            }
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return  false;
    }
    private void close() throws Exception{
        zk.close();
    }
    public List<String> getChildren (String path){
        List<String> childrens = new ArrayList<String>();
        try{

            childrens = zk.getChildren(path,zkClient);
            for (int i=0;i<childrens.size();i++){
                String nodename=childrens.get(i);
                System.out.println("#######"+nodename);
                getChildren(nodename);
            }
            if (StringUtils.isEmpty(path)) return null;
        }catch (Exception e){

        }
        return childrens;
    }

    /**
     * 创建ZooKeeper对象时, 只要对象完成初始化便立刻返回. 建立连接是以异步的形式进行的, 当连接成功建立后, 会回调watcher的process方法. 如果想要同步建立与server的连接, 需要自己进一步封装.
     * ZooKeeper 使用 WatchedEvent 对象封装服务端事件并传递给 Watcher， 从而方便回调方法 process 对服务端事件进行处理。  WatcherEvent 实体实现了序列化接口，因此可以用于网络传输.
     * Class WatcherEvent{
     * 		type:int
     * 		state:int
     * 		path:String
     * 	}
     *
     * 		state=-112 会话超时状态
     * 		state= -113 认证失败状态
     * 		state=  1 连接建立中
     * 		state= 2 (暂时不清楚如何理解这个状态,ZOO_ASSOCIATING_STATE)
     * 		state=3 连接已建立状态 state= 999 无连接状态
     * 		type=1 创建节点事件
     * 		type=2 删除节点事件
     * 		type=3 更改节点事件
     * 		type=4 子节点列表变化事件
     * 		type= -1 会话session事件
     * 		type=-2 监控被移除事件
     *
     *
     *
     * @param watchedEvent
     */

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            latch.countDown();;
        }
        else{
            System.out.println("持续堵塞...");
        }

    }

    public static void main(String[] args) {
        //PropertyConfigurator.configure("log4j.properties");
        String node="server1";
        //logger.info("zkClient建立之前++++");
        System.out.println("zkClient建立之前");
        ZKClient zkClient = new ZKClient();

        System.out.println("zkClient建立之后");
        try {
            zkClient.doStart(node);
        }catch (Exception e){
            System.out.println(e);
        }
        try{
            zkClient.close();
        }catch (Exception e){

        }


    }

}
