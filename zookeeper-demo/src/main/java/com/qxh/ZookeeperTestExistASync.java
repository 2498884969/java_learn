package com.qxh;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperTestExistASync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "192.168.1.60:2181";
    private static final String PREFIX_SYNC = "/mytest-async-exist8-";
    private static ZooKeeper zooKeeper ;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestExistASync());
        countDownLatch.await();
        zooKeeper.exists(PREFIX_SYNC, true, new IStaCallback(), null);
        zooKeeper.create(PREFIX_SYNC, "111".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zooKeeper.setData(PREFIX_SYNC, "222".getBytes(), -1);
//        //判断存在 并加监听
//        zooKeeper.exists(PREFIX_SYNC + "/c2", true, new IStaCallback(), null);
//        zooKeeper.create(PREFIX_SYNC + "/c2", "111".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zooKeeper.delete(PREFIX_SYNC + "/c2", -1);
////        zooKeeper.exists(PREFIX_SYNC , true, new IStaCallback(), null);
//        //上面修改数据 那步 对PREFIX_SYNC添加了一个监听
//        zooKeeper.delete(PREFIX_SYNC, -1);
        Thread.sleep(Integer.MAX_VALUE);

    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            try {
                if(Event.EventType.None == event.getType() && null == event.getPath()){
                    countDownLatch.countDown();
                }else if(Event.EventType.NodeDataChanged == event.getType()){
                    System.out.println("node " +  event.getPath() + " changed." );
                    zooKeeper.exists(event.getPath(), true, new IStaCallback(), null);
                }else if(Event.EventType.NodeCreated == event.getType()){
                    System.out.println("node " + event.getPath() + "  created.");
                    zooKeeper.exists(event.getPath(), true, new IStaCallback(), null);
                }else if(Event.EventType.NodeDeleted == event.getType()){
                    System.out.println("node " + event.getPath() + " deleted.");
                    zooKeeper.exists(event.getPath(), true, new IStaCallback(), null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
class IStaCallback implements AsyncCallback.StatCallback {
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("rc:" + rc + ",path: " + path + ",ctx:" + ctx + ", stat:" + stat);
    }
}