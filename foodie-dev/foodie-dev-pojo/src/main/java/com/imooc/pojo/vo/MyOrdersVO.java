package com.imooc.pojo.vo;

import java.util.Date;
import java.util.List;

public class MyOrdersVO {

    /**
     *              od.id as orderId,
     *             od.updated_time as updatedTime,
     *             od.pay_method as payMethod,
     *             od.real_pay_amount as realPayAmount,
     *             od.post_amount as postAmount,
     *             os.order_status as orderStatus
     */

    private String orderId;
    private Date updatedTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer orderStatus;
    private Integer isComment;

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    private List<MySubOrderItemVo> subOrderItemList;

    @Override
    public String toString() {
        return "MyOrdersVO{" +
                "orderId='" + orderId + '\'' +
                ", updatedTime=" + updatedTime +
                ", payMethod=" + payMethod +
                ", realPayAmount=" + realPayAmount +
                ", postAmount=" + postAmount +
                ", orderStatus=" + orderStatus +
                ", subOrderItemList=" + subOrderItemList +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(Integer realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public Integer getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(Integer postAmount) {
        this.postAmount = postAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<MySubOrderItemVo> getSubOrderItemList() {
        return subOrderItemList;
    }

    public void setSubOrderItemList(List<MySubOrderItemVo> subOrderItemList) {
        this.subOrderItemList = subOrderItemList;
    }
}
