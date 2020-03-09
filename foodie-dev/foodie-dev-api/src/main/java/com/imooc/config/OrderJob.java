package com.imooc.config;

import com.imooc.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderJob {

    @Autowired
    OrdersService ordersService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoClose () {

//        定时任务的弊端：
//          1. 会有时间差，程序不严谨

//          10:39下单，11：00检查不足1小时，12:00检查超过 1小时21分钟

//          2. 不支持集群
//          单机没毛病，使用集群后，就会有多个定时任务
//          解决方案：只是用一台计算机节点，单独用来运行所有的定时任务

//          3. 会对于数据库进行全表扫描，极其影响数据库的性能
//          解决方案：延时队列

        ordersService.closeOrder();
    }

}
