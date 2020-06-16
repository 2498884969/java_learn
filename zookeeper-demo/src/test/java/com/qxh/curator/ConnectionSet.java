package com.qxh.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionSet {

    public static final String ADDRESS = "192.168.1.60:2181,192.168.1.61:2181,192.168.1.62:2181";

    CuratorFramework client;

    @Before
    public void setUp() throws Exception {
        client = CuratorFrameworkFactory.builder()
                .connectString(ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryOneTime(3000))
                .namespace("set")
                .build();
        client.start();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void create() throws Exception {
        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node1", "node".getBytes());
        System.out.println("结束");
    }


    @Test
    public void set() throws Exception {

        client.setData()
                .withVersion(-1)
                .forPath("/node1", "node_1".getBytes());
        System.out.println("结束");
    }

    @Test
    public void set2() throws Exception {

        client.setData()
                .withVersion(1)
                .forPath("/node1", "node_2".getBytes());
        System.out.println("结束");
    }

    @Test
    public void set3() throws Exception {

        client.setData()
                .withVersion(-1)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println(event.getPath());
                        System.out.println(event.getType());
                    }
                })
                .forPath("/node1", "node_3".getBytes());
        Thread.sleep(3000);
        System.out.println("结束");
    }

}