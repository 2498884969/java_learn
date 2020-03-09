package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrdersService;
import com.imooc.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    Sid sid;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    AddressService addressService;

    @Autowired
    ItemService itemService;

    @Autowired
    OrderItemsMapper orderItemsMapper;

    @Autowired
    OrderStatusMapper orderStatusMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {

        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        Integer postAmount = 0;
        // 1. 订单数据保存
        Orders newOrder = new Orders();
        String orderId = sid.nextShort();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);

        UserAddress single = addressService.queryAddress(userId, addressId);
        newOrder.setReceiverName(single.getReceiver());
        newOrder.setReceiverMobile(single.getMobile());
        newOrder.setReceiverAddress(single.getProvince() + ' '
                                    + single.getCity() + ' '
                                    + single.getDistrict() + ' '
                                    + single.getDetail());

//        newOrder.setTotalAmount();
//        newOrder.setRealPayAmount();
        newOrder.setPostAmount(postAmount);

        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        // 2. 循环根据itemSpecIds保存商品信息表

        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;

        for (String specId: itemSpecIdArr) {

            int buyCounts = 1;
            // TODO 整合redis后，商品购买的信息重新从Redis中的购物车获取
            // 2.1 根据规格ID,查询规格具体信息,主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;


            // 2.2 根据商品id，获取商品信息(item)以及商品图片(url)
            String itemId =  itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgByItemId(itemId);

            OrderItems subOrder = new OrderItems();
            subOrder.setId(sid.nextShort());
            subOrder.setOrderId(orderId);
            subOrder.setItemId(itemId);
            subOrder.setItemImg(imgUrl);
            subOrder.setBuyCounts(buyCounts);
            subOrder.setItemName(items.getItemName());
            subOrder.setItemSpecName(itemsSpec.getName());
            subOrder.setPrice(itemsSpec.getPriceDiscount());
            subOrder.setItemSpecId(specId);
            // 2.3 循环保存子订单数据到数据库
            orderItemsMapper.insert(subOrder);
            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(specId, buyCounts);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);
        // 3. 保存订单状态表
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);

        // 4. 构建商户订单用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setPayMethod(payMethod);
        merchantOrdersVO.setAmount(realPayAmount+postAmount);

        // 5. 构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setOrderId(orderId);

        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus updateStatus = new OrderStatus();
        updateStatus.setOrderId(orderId);
        updateStatus.setOrderStatus(orderStatus);
        updateStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(updateStatus);
    }

    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {

        // 1. 删选出要关闭的订单
        OrderStatus orderToClosed = new OrderStatus();
        orderToClosed.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(orderToClosed);
        for (OrderStatus os: list) {
            Date createdTime = os.getCreatedTime();
            int days = DateUtil.daysBetween(createdTime, new Date());

            if (days>=1) {
                doCloseOrder(os.getOrderId());
            }

        }

        // 2. 循环对于订单进行关闭


    }

    public void doCloseOrder(String orderId) {

        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(close);

    }
}
