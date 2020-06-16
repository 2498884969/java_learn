package com.qxh;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

class MCallBack implements AsyncCallback.StatCallback {

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("rc:" + rc + ",path: " + path + ",ctx:" + ctx + ", stat:" + stat);
    }
}

public class ZookeeperTestExistAsync2 implements Watcher {

    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    private static String ADDRESS = "192.168.1.60:2181";
    public static final String PREFIX_TEST = "/mytest-qxh";

    public static void main(String[] args) throws Exception {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestExistAsync2());
        countDownLatch.await();
        zooKeeper.exists(PREFIX_TEST, true, new MCallBack(), null);
//        // 1.创建
        zooKeeper.create(PREFIX_TEST, "aaa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        // 2. 修改
        zooKeeper.setData(PREFIX_TEST, "hello world".getBytes(), -1);
//        // 3. 删除 ZooDefs.Ids.OPEN_ACL_UNSAFE
        zooKeeper.delete(PREFIX_TEST, -1);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {

        if(Event.KeeperState.SyncConnected == event.getState()){
            try {
                if(Event.EventType.None == event.getType() && null == event.getPath()){
                    countDownLatch.countDown();

                }else if(Event.EventType.NodeDataChanged == event.getType()){
                    System.out.println("node " +  event.getPath() + " changed." );
                    zooKeeper.exists(event.getPath(), true,  new MCallBack(), null);

                }else if(Event.EventType.NodeCreated == event.getType()){
                    System.out.println("node " + event.getPath() + "  created.");
                    zooKeeper.exists(event.getPath(), true,  new MCallBack(), null);

                }else if(Event.EventType.NodeDeleted == event.getType()){
                    System.out.println("node " + event.getPath() + " deleted.");
                    zooKeeper.exists(event.getPath(), true,  new MCallBack(), null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


