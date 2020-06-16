package com.qxh;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ZookeeperTestExistSync implements Watcher {

    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    private static String ADDRESS = "192.168.1.60:2181";
    public static final String PREFIX_TEST = "/mytest-qxh";

    public static void main(String[] args) throws Exception {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestExistSync());
        countDownLatch.await();
        Stat stat = zooKeeper.exists(PREFIX_TEST, true);
        System.out.println(stat);
        // 1.创建
        zooKeeper.create(PREFIX_TEST, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        // 2. 修改
        zooKeeper.setData(PREFIX_TEST, "hello world".getBytes(), -1);
        // 3. 删除 ZooDefs.Ids.OPEN_ACL_UNSAFE
        zooKeeper.delete(PREFIX_TEST, -1);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        Stat stat = null;

        if(Event.KeeperState.SyncConnected == event.getState()){
            try {
                if(Event.EventType.None == event.getType() && null == event.getPath()){
                    countDownLatch.countDown();

                }else if(Event.EventType.NodeDataChanged == event.getType()){
                    System.out.println("node " +  event.getPath() + " changed." );
                    stat = zooKeeper.exists(event.getPath(), true);

                }else if(Event.EventType.NodeCreated == event.getType()){
                    System.out.println("node " + event.getPath() + "  created.");
                    stat = zooKeeper.exists(event.getPath(), true);

                }else if(Event.EventType.NodeDeleted == event.getType()){
                    System.out.println("node " + event.getPath() + " deleted.");
                    stat = zooKeeper.exists(event.getPath(), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println(stat);
    }
}

