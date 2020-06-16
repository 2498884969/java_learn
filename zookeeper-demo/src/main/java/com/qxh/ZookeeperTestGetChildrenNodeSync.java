package com.qxh;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Watcher通知是一次性的，即一旦触发一次通知后，该Watcher就失效了，
 * 因此客户端需要反复注册Watcher，即程序中在process里面又注册了Watcher，
 * zooKeeper.getData(path,watch,stat),
 * 第二个参数值设为true则添加一次监听, 否则，将无法获取c3节点的创建而导致子节点变化的事件。
 */
public class ZookeeperTestGetChildrenNodeSync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "192.168.1.60:2181";
    private static final String PREFIX_SYNC = "/mytest-sync-getChild-";
    private static ZooKeeper zooKeeper ;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestGetChildrenNodeSync());
        countDownLatch.await();
        zooKeeper.create(PREFIX_SYNC, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zooKeeper.create(PREFIX_SYNC + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println(zooKeeper.getChildren(PREFIX_SYNC, true));
        zooKeeper.create(PREFIX_SYNC + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println(zooKeeper.getChildren(PREFIX_SYNC, true));
        Thread.sleep(1000);
        zooKeeper.create(PREFIX_SYNC + "/c3", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println(zooKeeper.getChildren(PREFIX_SYNC, true));
        Thread.sleep(Integer.MAX_VALUE);

    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(Event.EventType.NodeChildrenChanged == event.getType()){
                try {
                    System.out.println("get Child:" +
                            zooKeeper.getChildren(event.getPath(), false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
