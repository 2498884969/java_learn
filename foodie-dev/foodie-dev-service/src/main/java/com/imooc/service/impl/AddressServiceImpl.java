package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserAddressMapper userAddressMapper;

    @Autowired
    Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        // 1. 使用pojo对象作为条件进行查询
        UserAddress uaCondition = new UserAddress();
        uaCondition.setUserId(userId);
        return userAddressMapper.select(uaCondition);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {

        // 1. 判断当前用户地址是否存在，如果不存在则新增地址为默认地址
        Integer isDefault = 0;
        List<UserAddress> list = queryAll(addressBO.getUserId());
        if (list == null || list.isEmpty()) {
            isDefault = 1;
        }
        // 2.复制属性
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(sid.nextShort());
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {

        UserAddress pendingAddress = new UserAddress();

        BeanUtils.copyProperties(addressBO, pendingAddress);
        pendingAddress.setId(addressBO.getAddressId());
        pendingAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(pendingAddress);

    }

    @Override
    public void deleteUserAddress(String userId, String addressId) {

        UserAddress userAddressCondition = new UserAddress();
        userAddressCondition.setId(addressId);
        userAddressCondition.setUserId(userId);

        userAddressMapper.delete(userAddressCondition);

    }

    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {

        // 1. 将默认设置为非默认
        UserAddress userAddressCondition = new UserAddress();
        userAddressCondition.setUserId(userId);
        userAddressCondition.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> userAddresses = userAddressMapper.select(userAddressCondition);

        for (UserAddress userAddress: userAddresses) {
            userAddress.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(userAddress);
        }

        // 2. 根据主键将非默认设置为默认

        UserAddress addressDefault = new UserAddress();
        addressDefault.setId(addressId);
        addressDefault.setUserId(userId);
        addressDefault.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(addressDefault);




    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryAddress(String userId, String AddressId) {
        UserAddress single = new UserAddress();
        single.setUserId(userId);
        single.setId(AddressId);
        return userAddressMapper.selectOne(single);
    }
}
