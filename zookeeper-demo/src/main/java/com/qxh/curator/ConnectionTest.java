package com.qxh.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * {@link RetryPolicy }
 * {@link RetryOneTime } 只重试一次
 * {@link RetryNTimes}  重试N次
 * {@link RetryUntilElapsed} 在一定的时间内重试N次 RetryUntilElapsed(10000, 3000) 每三秒重试一次，超过10秒后停止
 * {@link ExponentialBackoffRetry}  ExponentialBackoffRetry(1000, 3) 重连三次但是重连的时间间隔是计算出的
 */
public class ConnectionTest {

    public static final String ADDRESS = "192.168.1.60:2181,192.168.1.61:2181,192.168.1.62:2181";

    public static void main(String[] args) {
        // 1. 创建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryOneTime(3000))
                .namespace("create")
                .build();

        // 2. 打开连接
        client.start();
        System.out.println(client.isStarted());
        // 3. 关闭连接
        client.close();
    }

    public void create() {}

}
