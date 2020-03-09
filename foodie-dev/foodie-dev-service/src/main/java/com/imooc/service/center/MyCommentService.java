package com.imooc.service.center;

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface MyCommentService {

    /**
     * 根据订单id查询与该订单相关的商品
     */
    List<OrderItems> queryPendingComments(String orderId);

    /**
     * 保存评论
     */
    void saveComments(String userId, String orderId, List<OrderItemsCommentBO> commentList);


    /**
     * 分页查询评论
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);

}
