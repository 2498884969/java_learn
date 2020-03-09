package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.service.center.MyCommentService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "用户中心-评价", tags = {"用户中心-评价"})
@RequestMapping("mycomments")
@RestController
public class MyCommentsController extends BaseController {

    @Autowired
    MyCommentService myCommentService;

    @ApiOperation(value = "获取要评论的订单的商品列表", notes = "获取要评论的订单的商品列表", httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(@RequestParam  String userId,
                                   @RequestParam String orderId) {
        // 1. 首先判断用户与订单是否存在关系，防止恶意攻击
        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        // 2. 判断当前订单是够已经评论过 YesOrNo, 评论过返回errorMsg
        Orders orders = (Orders) checkResult.getData();
        if (orders.getIsComment().equals(YesOrNo.YES.type)) {
            return IMOOCJSONResult.errorMsg("订单已经品论过");
        }
        // 3. 拿货
        List<OrderItems> list = myCommentService.queryPendingComments(orderId);
        return IMOOCJSONResult.ok(list);
    }

//    saveList

    @ApiOperation(value = "保存用户评论", notes = "保存用户评论", httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(@RequestParam  String userId,
                                    @RequestParam String orderId,
                                    @RequestBody List<OrderItemsCommentBO> orderItemsCommentBOS) {

        // 0. 判断 userId和orderId是否为空

        System.out.println(orderItemsCommentBOS);
        // 1. 首先判断用户与订单是否存在关系，防止恶意攻击
        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        // 2. 判断list是否为空

        if (orderItemsCommentBOS == null || orderItemsCommentBOS.isEmpty()) {
            return IMOOCJSONResult.errorMsg("评论列表为空");
        }

        myCommentService.saveComments(userId, orderId, orderItemsCommentBOS);

        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "获取我的订单", notes = "获取我的订单", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "分页页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页大小", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            IMOOCJSONResult.errorMsg("userId为空");
        }

        if (page==null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = myCommentService
                .queryMyComments(userId,page,pageSize);

        return IMOOCJSONResult.ok(result);
    }
}
