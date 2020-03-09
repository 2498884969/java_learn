package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {


    List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类查询子分类信息
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 根据rootCatId对于商品信息进行懒加载
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);

}
