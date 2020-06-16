package com.qxh.acl;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CustomAcl implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static final String ADDRESS = "192.168.1.60:2181";

    private static final String PREFIX = "/test0557";

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected.equals(event.getState()))
            countDownLatch.countDown();
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new CustomAcl());

        countDownLatch.await();

//        zooKeeper.create(PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        ACL acl = new ACL();

        Id mId  = new Id("ip","192.168.1.4");

        acl.setId(mId);
        acl.setPerms(ZooDefs.Perms.ALL);

        List<ACL> acls = new ArrayList<>();

        acls.add(acl);

        zooKeeper.create(PREFIX, "".getBytes(), acls, CreateMode.PERSISTENT);


        Thread.sleep(Integer.MAX_VALUE);


    }

}
