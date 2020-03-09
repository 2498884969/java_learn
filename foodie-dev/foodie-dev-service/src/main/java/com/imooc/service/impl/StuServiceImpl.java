package com.imooc.service.impl;

import com.imooc.service.StuService;
import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuServiceImpl implements StuService {

    @Autowired
    private StuMapper stuMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveStu() {
        Stu stu = new Stu();
        stu.setName("jack");
        stu.setAge(18);
        stuMapper.insert(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStu(int id) {

        Stu stu = stuMapper.selectByPrimaryKey(id);
        stu.setName("danny");
        stuMapper.updateByPrimaryKey(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteStu(int id) {
        stuMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveParent() {

        Stu stu = new Stu();
        stu.setName("parent");
        stu.setAge(99);
        stuMapper.insert(stu);

    }

    /**
     * 通过动态代理实现
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveChildren() {
        saveChild1();
        int b = 1/0;
        saveChild2();
    }

    public void saveChild1() {

        Stu stu = new Stu();
        stu.setName("child-1");
        stu.setAge(11);
        stuMapper.insert(stu);

    }

    public void saveChild2() {

        Stu stu = new Stu();
        stu.setName("child-2");
        stu.setAge(22);
        stuMapper.insert(stu);

    }


}
