package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户id获取所有的地址信息
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 添加新的用户地址信息
     */

    void addNewUserAddress(AddressBO addressBO);

    /**
     * 更新用户的地址信息
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除用户地址信息
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 将用户地址信息设为默认
     */
    void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据addressId和UserId查询出地址信息
     */
    UserAddress queryAddress(String userId, String AddressId);

}
