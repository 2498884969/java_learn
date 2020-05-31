package com.qxh.item.controller;


import com.qxh.item.pojo.Item;
import com.qxh.item.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api("item-controller")
@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 对外提供接口服务，查询商品信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取商品详情")
    @ApiImplicitParam(name = "id", value = "商品名称", required = true,dataType = "String")
    @GetMapping(value = "item/{id}")
    public Item queryItemById(@PathVariable("id") Long id) {
        return this.itemService.queryItemById(id);
    }

    /**
     * 健康检查
     * @return
     */
    @ApiIgnore
    @RequestMapping("/health")
    public String healthCheck() {
        return "OK";
    }

}
