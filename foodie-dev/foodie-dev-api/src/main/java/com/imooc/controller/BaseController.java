package com.imooc.controller;

import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.UUID;

public class BaseController {

    @Autowired
    protected MyOrdersService myOrdersService;

    @Autowired
    RedisOperator redisOperator;

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    public static final Integer COMMON_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    // 微信支付->支付成功->天天吃货平台
    public static final String payReturnUrl = "http://jau287.natappfree.cc/orders/notifyMerchantOrderPaid";

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 用户上传头像的位置 /Users/qiangxuhui
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "Users" +
            File.separator + "qiangxuhui" +
            File.separator + "images" +
            File.separator + "foodie" +
            File.separator + "faces";

    /**
     * 查看订单与用户间是否存在关联关系
     * @param userId
     * @param orderId
     * @return
     */
    protected IMOOCJSONResult checkUserOrder(String userId, String orderId) {

        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return IMOOCJSONResult.errorMsg("订单不存在");
        }

        return IMOOCJSONResult.ok(order);

    }

    protected UsersVO convertUserVO(Users result) {
        String token_key = REDIS_USER_TOKEN + ":" + result.getId();
        String unique_token = UUID.randomUUID().toString().trim();
        redisOperator.set(token_key, unique_token);
        //  填充cookies中的数据
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(result, usersVO);
        usersVO.setUserUniqueToken(unique_token);
        return usersVO;
    }
}
