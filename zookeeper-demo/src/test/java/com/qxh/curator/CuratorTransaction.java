package com.qxh.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryOneTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorTransaction {

    public static final String ADDRESS = "192.168.1.60:2181,192.168.1.61:2181,192.168.1.62:2181";

    CuratorFramework client;

    @Before
    public void setUp() throws Exception {
        client = CuratorFrameworkFactory.builder()
                .connectString(ADDRESS)
                .sessionTimeoutMs(5000)
                .namespace("create")
                .retryPolicy(new RetryOneTime(3000))
                .build();
        client.start();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void watch1() throws Exception {
        client.inTransaction()
                .create().forPath("/node1", "node1".getBytes())
                .and()
                .setData().forPath("/node2", "node2".getBytes())
                .and()
                .commit();
    }





}