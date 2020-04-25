package com.imooc.service;

import com.imooc.utils.PagedGridResult;

public interface ItemService {

    PagedGridResult searchItems(String keywords,String sort,Integer page,Integer pageSize);

}
