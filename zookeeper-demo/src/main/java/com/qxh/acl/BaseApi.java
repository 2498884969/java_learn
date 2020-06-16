package com.qxh.acl;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class BaseApi implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static final String ADDRESS = "192.168.1.60:2181";

    private static final String PREFIX = "/test0554";

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected.equals(event.getState()))
            countDownLatch.countDown();
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new BaseApi());

        countDownLatch.await();

        zooKeeper.create(PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        Thread.sleep(Integer.MAX_VALUE);


    }

}
