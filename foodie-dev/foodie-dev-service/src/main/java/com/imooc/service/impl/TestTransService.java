package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTransService {

    @Autowired
    StuMapper stuMapper;

    @Autowired
    StuService stuService;


    /**
     * PROPERGATION_REQUIRED:   不存在事务则创建一个事务, 存在则加入
     * PROPERGATION_SUPPORT:    存在事务则加入，不存在也可以，不会自己去创建
     * PROPERGATION_MANDATORY:　方法必须运行在一个事务中，不存在事务则抛出异常
     * 事务在同一个java类中不传播
     * PROPERGATION_REQUIRES_NEW:  新建一个自己的事务，不论当前是否存在事务,如果当前事务存在则挂起当前事务。
     * PROPERGATION_NOT_SUPPORT: 当前存在事务则将其 挂起
     * PROPERGATION_NEVER: 当前方法不能运行在事务中，存在事务则抛出异常
     * PROPERGATION_NESTED: 存在事务则作为子事务运行在嵌套事务中，不存在则创建一个事务，
     * 子事务的运行如果失败被捕捉，父事务正常运行
     *
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void testPropagationTrans(){

        stuService.saveParent();
        try {
            stuService.saveChildren();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
