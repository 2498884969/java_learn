package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom{

    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 根据rootCatId对于商品信息进行懒加载
     */
    List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);

}