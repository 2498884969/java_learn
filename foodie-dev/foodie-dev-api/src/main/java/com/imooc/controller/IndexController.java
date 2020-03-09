package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Api(value = "首页", tags = {"首页相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    CarouselService carouselService;

    @Autowired
    CategoryService categoryService;

    @ApiIgnore
    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        session.setMaxInactiveInterval(3600);

        return "ok";
    }


    @ApiOperation(value = "获取轮播图列表", notes = "获取轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {

        List<Carousel> carouselList = carouselService.queryAll(YesOrNo.YES.type);
        return IMOOCJSONResult.ok(carouselList);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次仅加载大分类
     * 2. 当鼠标滑动到大分类的时候再去加载该分类的子分类，如果子分类已经被加载，则不再发起请求
     */

    @ApiOperation(value = "获取所有根分类", notes = "获取所有根分类", httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats() {
        List<Category> categoryList = categoryService.queryAllRootLevelCat();
        return IMOOCJSONResult.ok(categoryList);
    }

    /**
     * 根据rootCatId 获取其余分类
     */
    @ApiOperation(value = "根据rootCatId 获取其余分类", notes = "根据rootCatId 获取其余分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类的id", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId==null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }

        return IMOOCJSONResult.ok(categoryService.getSubCatList(rootCatId));
    }

    /**
     * 根据rootCatId最新商品信息
     */
    @ApiOperation(value = "根据rootCatId最新商品信息", notes = "根据rootCatId最新商品信息", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类的id", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId==null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }

        return IMOOCJSONResult.ok(categoryService.getSixNewItemsLazy(rootCatId));
    }

}
