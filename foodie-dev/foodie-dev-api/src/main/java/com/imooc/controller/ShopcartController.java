package com.imooc.controller;


import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@Api(value = "购物车", tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    public static final Logger LOGGER = LoggerFactory.getLogger(ShopcartController.class);


    @ApiOperation(value = "向购物车中添加商品", notes = "向购物车中添加商品", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBO) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("userId为空");
        }

        System.out.println(shopcartBO);

        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "判断用户是否存在", notes = "判断用户是否存在", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        LOGGER.info("删除商品"+ itemSpecId);

        // TODO 前端用户在登录的情况下，删除购物车中的商品，同时会同步到缓存当中去

        return IMOOCJSONResult.ok();
    }

}
