package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品ID查询详情
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品ID查询商品图片列表
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品ID查询商品规格
     */
    List<ItemsSpec> queryItemsSpecList(String itemId);

    /**
     * 根据商品ID查询商品参数(生产厂商等)
     */
    ItemsParam queryItemsParam(String itemId);

    /**
     * 根据商品ID查询商品评价等级分类数量
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品的itemId对于商品评论进行分页查询
     */
    PagedGridResult queryPagedComments(String itemId, Integer level,
                                       Integer page, Integer pageSize);

    /**
     * 根据关键字对于商品进行分页查询
     */
    PagedGridResult searchItems(String keywords, String sort,
                                Integer page, Integer pageSize);

    /**
     * 根据分类id搜索商品列表
     */
    PagedGridResult searchItems(Integer catId, String sort,
                                      Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新购物车中的商品数据（用于刷新渲染购物车中的商品数据）
     */

    List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品的规格id获取商品规格
     */
    ItemsSpec queryItemSpecById(String itemSpecId);

    /**
     * 根据商品id获取商品主图链接
     */
    String queryItemMainImgByItemId(String itemId);

    /**
     * 根据商品的规格id对于固定规格的商品进行削减
     */

    void decreaseItemSpecStock(String specId, Integer buyCounts);

}
