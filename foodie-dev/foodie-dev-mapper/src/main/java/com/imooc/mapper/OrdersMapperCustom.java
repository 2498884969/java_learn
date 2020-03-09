package com.imooc.mapper;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    /**
     * 根据状态查询我的订单
     * @param map
     * @return
     */
     List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    /**
     *  根据订单状态查询数量
     * @return
     */
     int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);


    /**
     * 获取订单动向
     * @param map
     * @return
     */
     List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}