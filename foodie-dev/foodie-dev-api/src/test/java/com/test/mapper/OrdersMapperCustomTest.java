package com.test.mapper;

import com.imooc.Application;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.vo.MyOrdersVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class OrdersMapperCustomTest {

    @Autowired
    OrdersMapperCustom ordersMapperCustom;

//    @Test
    public void queryMyOrders() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", "1908017YR51G1XWH");
        paramsMap.put("orderStatus", OrderStatusEnum.CLOSE.type);
        List<MyOrdersVO> myOrdersVOS = ordersMapperCustom.queryMyOrders(paramsMap);
        for (MyOrdersVO ordersVO: myOrdersVOS) {
            System.out.println(ordersVO);
        }
//        System.out.println(myOrdersVOS);
    }

}
