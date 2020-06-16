package com.qxh.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MyLock {

    // 1. 连接
    final String ADDRESS = "192.168.1.60:2181";
    // 2. countdown
    final CountDownLatch countDownLatch = new CountDownLatch(1);

    // 3. lock_root_path lock_node_name lockpath
    private static final String LOCK_ROOT_PATH = "/Locks";
    private static final String LOCK_NODE_NAME = "Lock_";
    private String lockPath;

    // 4.
    ZooKeeper zooKeeper;
    public MyLock(){
        try {
            zooKeeper = new ZooKeeper(ADDRESS, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState().equals(Event.KeeperState.SyncConnected)){
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void acquireLock() throws Exception{
        createLock();
        attemptLock();
    }

    public void createLock() throws Exception{
        // 1. 判断locks是否存在
        Stat stat = zooKeeper.exists(LOCK_ROOT_PATH, false);
        if (stat==null){
            zooKeeper.create(LOCK_ROOT_PATH,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        // 2. 创建临时有序节点
        lockPath = zooKeeper.create(LOCK_ROOT_PATH + "/"+LOCK_NODE_NAME,"".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    Watcher lockReleaseWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType().equals(Event.EventType.NodeDeleted)){
                synchronized(this) {
                    this.notifyAll();
                }
            }
        }
    };

    public void attemptLock() throws Exception{
        // 1. 获取/Locks下所有的锁节点
        List<String> list = zooKeeper.getChildren(LOCK_ROOT_PATH, false);
        // 2. 对于锁节点进行排序
        Collections.sort(list);
        // 3. 找出当前所节点的位置，如果位置为0 则获取锁，不为0则对前一个对象添加监视器，
        int index = list.indexOf(lockPath.substring(LOCK_ROOT_PATH.length()+1));
        // 4. 若果stat为空则继续获取锁，为非空则进行阻塞等待
        if (index==0){
            System.out.println("获取锁成功:"+lockPath);
            return;
        }else {
            String path = list.get(index-1);
            Stat stat = zooKeeper.exists(LOCK_ROOT_PATH+"/"+path, lockReleaseWatcher);
            if (stat==null){
                attemptLock();
            } else{
                synchronized (lockReleaseWatcher){
                    lockReleaseWatcher.wait();
                    // todo 都应该去监视第一个，不然的话万一中间的某个检点宕机就会导致出现多个锁的情况
                }
                attemptLock();
            }
        }

    }

    public void releaseLock() throws Exception{

        // 1. 删除临时有序节点  关闭zookeeper
        zooKeeper.delete(lockPath, -1);
        zooKeeper.close();
        System.out.println("锁已经释放："+lockPath);

    }

    public static void main(String[] args) throws Exception {
        MyLock lock = new MyLock();
        lock.createLock();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
