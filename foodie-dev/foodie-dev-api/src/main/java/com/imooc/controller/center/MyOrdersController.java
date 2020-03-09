package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.center.CenterUserService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户中心-订单相关")
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @ApiOperation(value = "获取我的订单", notes = "获取我的订单", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam Integer orderStatus,
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

        PagedGridResult result = myOrdersService.queryMyOrders(userId,orderStatus,page,pageSize);
        return IMOOCJSONResult.ok(result);
    }

    @GetMapping("/deliver")
    @ApiOperation(value="更新状态：待发货-》已发货", notes = "更新状态：待发货-》已发货", httpMethod = "GET")
    public IMOOCJSONResult deliver(@RequestParam String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单号为空");
        }

        myOrdersService.updateDeliverStatus(orderId);

        return IMOOCJSONResult.ok();
    }

    @PostMapping("/confirmReceive")
    @ApiOperation(value="确认收货", notes = "确认收货", httpMethod = "GET")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        IMOOCJSONResult checkResult  = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return IMOOCJSONResult.errorMsg("订单确认收货失败");
        }
        return IMOOCJSONResult.ok();

    }

    @PostMapping("/delete")
    @ApiOperation(value="删除订单", notes = "删除订单", httpMethod = "GET")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        IMOOCJSONResult checkResult  = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }

        boolean res = myOrdersService.deleteOrder(orderId, userId);
        if (!res) {
            return IMOOCJSONResult.errorMsg("订单删除失败");
        }
        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "用户中心-订单-订单状态计数", notes = "用户中心-订单-订单状态计数", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public IMOOCJSONResult statusCounts (@RequestParam String userId){

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("用户ID为空");
        }

        OrderStatusCountsVO result = myOrdersService.getMyOrderStatusCounts(userId);

        return IMOOCJSONResult.ok(result);
    }


    @ApiOperation(value = "用户中心-获取订单动态", notes = "用户中心-获取订单动态", httpMethod = "POST")
    @PostMapping("/trend")
    public IMOOCJSONResult trend(
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

        PagedGridResult result = myOrdersService.getMyOrdersTrend(userId, page, pageSize);

        return IMOOCJSONResult.ok(result);

    }

}
