package com.imooc.controller;


import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(value = "购物车", tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController extends BaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(ShopcartController.class);

    @Autowired
    RedisOperator redisOperator;


    @ApiOperation(value = "向购物车中添加商品", notes = "向购物车中添加商品", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBO) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("userId为空");
        }

        String key = FOODIE_SHOPCART + ":" + userId;
        String shopcartListStr = redisOperator.get(key);
        List<ShopcartBO> shopcartBOS;

        if (!StringUtils.isBlank(shopcartListStr)) {
            shopcartBOS =  JsonUtils.jsonToList(shopcartListStr, ShopcartBO.class);
            // 判断商品是否存在
            boolean isHave = false;
            for(ShopcartBO sc: shopcartBOS) {
                String specId = sc.getSpecId();
                if (specId.equals(shopcartBO.getSpecId())){
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHave = true;
                }
            }
            // 覆盖
            if (!isHave) {
                shopcartBOS.add(shopcartBO);
            }
        }else{
            // 存库
            shopcartBOS = new ArrayList<>();
            shopcartBOS.add(shopcartBO);
        }
        // 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        // 如果商品已经存在则进行累加
        redisOperator.set(key, JsonUtils.objectToJson(shopcartBOS));

        return IMOOCJSONResult.ok(shopcartBOS);
    }

    @ApiOperation(value = "判断用户是否存在", notes = "判断用户是否存在", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        String key = FOODIE_SHOPCART + ":" + userId;

        String shopCartListStr = redisOperator.get(key);
        if (!StringUtils.isBlank(shopCartListStr)) {
            // 1.转
            List<ShopcartBO> shopcartBOList = JsonUtils.jsonToList(shopCartListStr, ShopcartBO.class);
            // 2. 删
            shopcartBOList.removeIf( (s) -> s.getSpecId().equals(itemSpecId));
            // 3. 写redis
            redisOperator.set(key, JsonUtils.objectToJson(shopcartBOList));
        }

        // 前端用户在登录的情况下，删除购物车中的商品，同时会同步到缓存当中去

        return IMOOCJSONResult.ok();
    }

}
