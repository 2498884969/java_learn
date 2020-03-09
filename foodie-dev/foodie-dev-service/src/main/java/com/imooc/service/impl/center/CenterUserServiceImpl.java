package com.imooc.service.impl.center;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.CenterUserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    Sid sid;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setId(userId);
        users.setPassword(null);
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {

        // 1. 进行属性拷贝
        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO, updateUser);
        // 2. 设置userId和更新时间
        updateUser.setId(userId);
        updateUser.setCreatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        // 3. 返回更新后的结果

        return usersMapper.selectByPrimaryKey(userId);

    }

    @Override
    public Users updateUserFace(String userId, String faceUrl) {

        Users updateUser = new Users();
        // 1. 设置userId和更新时间 头像的链接
        updateUser.setId(userId);
        updateUser.setCreatedTime(new Date());
        updateUser.setFace(faceUrl);

        usersMapper.updateByPrimaryKeySelective(updateUser);
        // 2. 返回更新后的结果

        return usersMapper.selectByPrimaryKey(userId);
    }


}
