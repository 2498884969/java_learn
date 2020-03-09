package com.imooc.service;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

public interface OrdersService {

    /**
     * 创建订单
     * @param submitOrderBO
     */
    OrderVO createOrder(SubmitOrderBO submitOrderBO);

    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 根据订单ID获取订单状态
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付的订单
     */
    void closeOrder();

}
