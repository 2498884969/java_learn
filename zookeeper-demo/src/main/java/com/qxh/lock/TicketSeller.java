package com.qxh.lock;


import java.util.Random;

public class TicketSeller {

    // 1. 卖票

    public void sell() throws InterruptedException {
        int key = new Random().nextInt();
        System.out.println("开始买票--"+key);
        Thread.sleep(5000);
        System.out.println("买票成功--"+key);
    }

    // 2. 加锁卖票

    public void sellSync() throws Exception {
        MyLock lock = new MyLock();
        lock.acquireLock();
        sell();
        lock.releaseLock();
    }

    public static void main(String[] args) throws Exception {

        TicketSeller seller = new TicketSeller();
        Thread.sleep(10);

        for (int i = 0; i < 10; i++) {

            Thread.sleep(3000);
            seller.sellSync();

//            new Thread(() -> {
//                try {
//                    Thread.sleep(3);
//                    seller.sellSync();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
        }
    }

}
