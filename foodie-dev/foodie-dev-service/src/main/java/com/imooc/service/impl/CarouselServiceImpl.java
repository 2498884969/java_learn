package com.imooc.service.impl;

import com.imooc.mapper.CarouselMapper;
import com.imooc.pojo.Carousel;
import com.imooc.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    CarouselMapper carouselMapper;

    @Override
    public List<Carousel> queryAll(Integer isShow) {

        // 通过Example进行查询
        Example carouselExample = new Example(Carousel.class);
        carouselExample.orderBy("sort").desc();

        Example.Criteria carouselCriteria = carouselExample.createCriteria();

        carouselCriteria.andEqualTo("isShow", isShow);

        List<Carousel> carouselList = carouselMapper.selectByExample(carouselExample);

        return carouselList;
    }
}
