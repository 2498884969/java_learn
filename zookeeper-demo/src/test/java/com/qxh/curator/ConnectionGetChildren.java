package com.qxh.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class ConnectionGetChildren {

    public static final String ADDRESS = "192.168.1.60:2181,192.168.1.61:2181,192.168.1.62:2181";

    CuratorFramework client;

    @Before
    public void setUp() throws Exception {
        client = CuratorFrameworkFactory.builder()
                .connectString(ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryOneTime(3000))
                .build();
        client.start();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }


    @Test
    public void getChildren() throws Exception {

        List<String> list = client.getChildren()
                .forPath("/create");
        System.out.println(list);
    }



}