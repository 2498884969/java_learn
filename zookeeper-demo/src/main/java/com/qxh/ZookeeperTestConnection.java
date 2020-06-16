package com.qxh;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

// 1. 参考https://www.cnblogs.com/shamo89/p/9787176.html

public class ZookeeperTestConnection implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static final String ADDRESS = "192.168.1.60:2181";

    public void process(WatchedEvent watchedEvent) {

        System.out.println("receive the event:"+watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState())
            countDownLatch.countDown();


    }

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestConnection());
        System.out.println(zooKeeper.getState());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("zookeeper session established");
    }
}
