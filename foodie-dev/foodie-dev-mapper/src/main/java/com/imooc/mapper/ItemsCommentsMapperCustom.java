package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.MyCommentVO;
import com.imooc.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom {

    /**
     * commentList, userId
     * @param map
     */
    void saveComments(Map<String, Object> map);

    /**
     * 用户中心-查询评论
     */
    List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}