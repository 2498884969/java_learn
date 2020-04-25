package com.imooc.controller;

import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/items")
@RestController
public class ItemsController {

    @Autowired
    ItemService itemService;

    @ApiOperation(value = "根据关键字来搜索商品", notes = "根据关键字来搜索商品", httpMethod = "GET")
    @GetMapping("/es/search")
    public IMOOCJSONResult search(
            String keywords,
            String sort,
            Integer page,
            Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            IMOOCJSONResult.errorMsg("keywords为空");
        }

        if (page==null) {
            page = 1;
        }
        page --;

        if (pageSize == null) {
            pageSize = 20;
        }
        PagedGridResult result = itemService.searchItems(keywords,sort,page,pageSize);

        return IMOOCJSONResult.ok(result);
    }
}
