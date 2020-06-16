package com.qxh.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.RetryOneTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorLock {

    public static final String ADDRESS = "192.168.1.60:2181,192.168.1.61:2181,192.168.1.62:2181";

    CuratorFramework client;

    @Before
    public void setUp() throws Exception {
        client = CuratorFrameworkFactory.builder()
                .connectString(ADDRESS)
                .sessionTimeoutMs(5000)
                .namespace("lock")
                .retryPolicy(new RetryOneTime(3000))
                .build();
        client.start();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void lock1() throws Exception {
        // 1. 互斥锁
        InterProcessLock interProcessLock = new InterProcessMutex(client, "/lock1");

        interProcessLock.acquire();
        System.out.println("获取互斥锁");
        for (int i = 0; i <10 ; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        System.out.println("释放互斥锁");
        interProcessLock.release();
    }

    @Test
    public void lock2() throws Exception {
        // 2. 读写锁--读锁
        InterProcessReadWriteLock interProcessReadWriteLock =  new InterProcessReadWriteLock(client, "/lock1");
        InterProcessLock interProcessReadLock = interProcessReadWriteLock.readLock();
        interProcessReadLock.acquire();

        System.out.println("获取读锁");
        for (int i = 0; i <10 ; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        System.out.println("释放读锁");
        interProcessReadLock.release();
    }

    @Test
    public void lock3() throws Exception {
        // 3. 读写锁--写锁

        InterProcessReadWriteLock interProcessReadWriteLock =  new InterProcessReadWriteLock(client, "/lock1");
        InterProcessLock interProcessWriteLock = interProcessReadWriteLock.writeLock();
        interProcessWriteLock.acquire();

        System.out.println("获取写锁");
        for (int i = 0; i <10 ; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        System.out.println("释放写锁");
        interProcessWriteLock.release();

    }




}