package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.utils.PagedGridResult;

public interface MyOrdersService {

    /**
     *  分页查询订单
     */
    PagedGridResult queryMyOrders (String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 更新订单状态  待发货->已收货
     */
    void updateDeliverStatus(String orderId);


    /**
     * 查询关联订单
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 将订单的状态从  已收货-》交易成功
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单（逻辑删除）
     */
    boolean deleteOrder(String orderId, String userId);

    /**
     * 获取订单状态数量
     */
    OrderStatusCountsVO getMyOrderStatusCounts(String userId);

    /**
     * 查询订单动态
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult getMyOrdersTrend (String userId,  Integer page, Integer pageSize);

}
