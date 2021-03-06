package com.imooc.controller;


import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrdersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "订单相关", tags = {"订单相关接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisOperator redisOperator;

    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        if (!submitOrderBO.getPayMethod().equals(PayMethod.WEIXIN.type) &&
                !submitOrderBO.getPayMethod().equals(PayMethod.ALIPAY.type)) {
            return IMOOCJSONResult.errorMsg("非法的支付方式");
        }

        // 获取购物车中的商品
        String key = FOODIE_SHOPCART + ":" + submitOrderBO.getUserId();
        String shopCartListStr = redisOperator.get(key);
        if (StringUtils.isBlank(shopCartListStr)) {
            return IMOOCJSONResult.errorMsg("购物车数据有误");
        }

        List<ShopcartBO> shopcartBOList = JsonUtils.jsonToList(shopCartListStr, ShopcartBO.class);

        // 1. 创建订单
        OrderVO orderVO = ordersService.createOrder(shopcartBOList, submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 移除购物车中的商品
        shopcartBOList.removeAll(orderVO.getToBeRemovedShopCartList());
        // 3. 刷新redis中的数据
        redisOperator.set(key, JsonUtils.objectToJson(shopcartBOList));

        // 4. 创建订单后移除购物车中已结算的商品     cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART,
                JsonUtils.objectToJson(shopcartBOList),true);
        // 5. 向支付中心发送订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为了方便测试所有的测试金额均为1分钱
        merchantOrdersVO.setAmount(1);


        HttpHeaders httpHeaders  = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("imoocUserId", "5877511-2498884969");
        httpHeaders.set("password", "dwer-40lr-40tk-32fe");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, httpHeaders);
        ResponseEntity<IMOOCJSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, IMOOCJSONResult.class);
        IMOOCJSONResult paymentResult = responseEntity.getBody();

        if (paymentResult.getStatus() != 200){
            return IMOOCJSONResult.errorMsg("订单支付失败");
        }




        return IMOOCJSONResult.ok(orderId);
    }

    @ApiOperation(value = "用于被支付中心回调的接口", notes = "用于被支付中心回调的接口", httpMethod = "POST")
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(@RequestParam String merchantOrderId){
        ordersService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "查询订单状态", notes = "查询订单状态", httpMethod = "POST")
    @PostMapping("/getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(@RequestParam String orderId) {

        OrderStatus orderStatus = ordersService.queryOrderStatusInfo(orderId);

        return IMOOCJSONResult.ok(orderStatus);
    }

}
