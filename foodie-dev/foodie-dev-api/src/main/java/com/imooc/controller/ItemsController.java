package com.imooc.controller;

import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品详情", tags = {"商品详情接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {


    @Autowired
    ItemService itemService;
    /**
     * 根据itemId最新商品信息
     */
    @ApiOperation(value = "根据itemId获取商品信息", notes = "根据itemId获取商品信息", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {

        if (StringUtils.isBlank(itemId)) {
            IMOOCJSONResult.errorMsg("itemId为空");
        }

        ItemInfoVO itemInfoVO = new ItemInfoVO(
                itemService.queryItemById(itemId),
                itemService.queryItemImgList(itemId),
                itemService.queryItemsSpecList(itemId),
                itemService.queryItemsParam(itemId));

        return IMOOCJSONResult.ok(itemInfoVO);

    }

    /**
     * 根据itemId获取商品评价数量的统计结果
     */
    @ApiOperation(value = "根据itemId获取商品评价数量的统计结果", notes = "根据itemId获取商品评价数量的统计结果", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam("itemId") String itemId) {

        if (StringUtils.isBlank(itemId)) {
            IMOOCJSONResult.errorMsg("itemId为空");
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "获取评论信息", notes = "获取评论信息", httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评论等级1、2、3", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "分页页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页大小", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            IMOOCJSONResult.errorMsg("itemId为空");
        }

        if (page==null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = itemService.queryPagedComments(itemId,level,page,pageSize);
        return IMOOCJSONResult.ok(result);
    }

    @ApiOperation(value = "根据关键字来搜索商品", notes = "根据关键字来搜索商品", httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult search(
            @ApiParam(name = "keywords", value = "搜索关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序方式{c:销量、p:价格、k:默认}", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "分页页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页大小", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            IMOOCJSONResult.errorMsg("keywords为空");
        }

        if (page==null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult result = itemService.searchItems(keywords,sort,page,pageSize);

        return IMOOCJSONResult.ok(result);
    }

    @ApiOperation(value = "根据三级分类id来搜索商品", notes = "根据三级分类id来搜索商品", httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(
            @ApiParam(name = "catId", value = "catId", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序方式{c:销量、p:价格、k:默认}", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "分页页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页大小", required = false)
            @RequestParam Integer pageSize) {


        if (catId==null) {
            return IMOOCJSONResult.errorMsg("catId为空");
        }

        if (page==null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult result = itemService.searchItems(catId,sort,page,pageSize);

        return IMOOCJSONResult.ok(result);
    }

    /**
     * 根据specIds获取购物车中的商品信息（刷新渲染）
     */
    @ApiOperation(value = "根据itemId获取商品评价数量的统计结果", notes = "根据itemId获取商品评价数量的统计结果", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "列表itemSpecIds", required = true, example = "1, 2, 4")
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            IMOOCJSONResult.ok();
        }

        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(list);
    }

}
