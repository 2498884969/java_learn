package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.service.impl.BaseService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyOrdersServiceImpl extends BaseService implements MyOrdersService  {

    @Autowired
    OrdersMapperCustom ordersMapperCustom;

    @Autowired
    OrderStatusMapper orderStatusMapper;

    @Autowired
    OrdersMapper ordersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        if (orderStatus != null){
            paramsMap.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);

        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(paramsMap);

        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverStatus(String orderId) {

        // 1. 更新设置时间、状态
        OrderStatus updateStatus = new OrderStatus();
        updateStatus.setDeliverTime(new Date());
        updateStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        // 2. example条件设置 orderId, status-20
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateStatus, example);

    }

    @Override
    public Orders queryMyOrder(String userId, String orderId) {

        Orders record = new Orders();
        record.setId(orderId);
        record.setUserId(userId);
        record.setIsDelete(YesOrNo.NO.type);

        return ordersMapper.selectOne(record);
    }

    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        // 1. 设置确认收货的时间、状态
        OrderStatus updateStatus = new OrderStatus();
        updateStatus.setSuccessTime(new Date());
        updateStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        // 2. 设置example 根据订单id 订单状态 已收货
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        // 3. 返回是否更新成功
        int res = orderStatusMapper.updateByExampleSelective(updateStatus, example);
        return res == 1;
    }

    @Override
    public boolean deleteOrder(String orderId, String userId) {

        // 1. 设置订单软删除状态以及更新时间
        Orders updateOrders = new Orders();
        updateOrders.setIsDelete(YesOrNo.YES.type);
        updateOrders.setUpdatedTime(new Date());
        // 2. 设置订单id和用户id
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId", userId);
        // 3. 返回是否更新成功
//        int res = ordersMapper.updateByExample(updateOrders, example);
        int res = ordersMapper.updateByExampleSelective(updateOrders, example);

        return res == 1;
    }

    @Override
    public OrderStatusCountsVO getMyOrderStatusCounts(String userId) {
        // 1.
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);

        paramsMap.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts(paramsMap);

        paramsMap.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(paramsMap);

        paramsMap.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(paramsMap);

        paramsMap.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        paramsMap.put("isComment", YesOrNo.NO.type);
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(paramsMap);

        OrderStatusCountsVO orderStatusCountsVO = new OrderStatusCountsVO();
        orderStatusCountsVO.setWaitPayCounts(waitPayCounts);
        orderStatusCountsVO.setWaitDeliverCounts(waitDeliverCounts);
        orderStatusCountsVO.setWaitReceiveCounts(waitReceiveCounts);
        orderStatusCountsVO.setWaitCommentCounts(waitCommentCounts);

        return orderStatusCountsVO;

    }

    @Override
    public PagedGridResult getMyOrdersTrend(String userId, Integer page, Integer pageSize) {


        PageHelper.startPage(page, pageSize);


        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<OrderStatus> list = ordersMapperCustom.getMyOrderTrend(map);

        return setterPagedGrid(list, page);

    }

}
