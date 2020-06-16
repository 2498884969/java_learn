package com.qxh;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 在更新数据时，setData方法存在一个version参数，其用于指定节点的数据版本，表明本次更新操作是针对指定的数据版本进行的，
 * 但是，在getData方法中，并没有提供根据指定数据版本来获取数据的接口，那么，这里为何要指定数据更新版本呢，
 * 这里方便理解，可以等效于CAS（compare and swap），
 * 对于值V，每次更新之前都会比较其值是否是预期值A，只有符合预期，才会将V原子化地更新到新值B。
 * Zookeeper的setData接口中的version参数可以对应预期值，表明是针对哪个数据版本进行更新，
 * 假如一个客户端试图进行更新操作，它会携带上次获取到的version值进行更新，而如果这段时间内，
 * Zookeeper服务器上该节点的数据已经被其他客户端更新，那么其数据版本也会相应更新，
 * 而客户端携带的version将无法匹配，无法更新成功，因此可以有效地避免分布式更新的并发问题。
 */

public class ZookeeperTestSetNodeDataASync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "192.168.1.60:2181";
    private static final String PREFIX_SYNC = "/mytest-async-setData4-";
    private static ZooKeeper zooKeeper ;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestSetNodeDataASync());
        countDownLatch.await();
        zooKeeper.create(PREFIX_SYNC, "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.setData(PREFIX_SYNC, "hello2".getBytes(), -1, new StatCallback(), null);
        Thread.sleep(Integer.MAX_VALUE);

    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }
        }
    }

}
class StatCallback implements AsyncCallback.StatCallback {
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("rc:"+rc + ", path:" + path + ",ctx:" + ctx + ", stat:" + stat);
    }
}
