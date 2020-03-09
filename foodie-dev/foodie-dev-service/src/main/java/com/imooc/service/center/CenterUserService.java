package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;

public interface CenterUserService {

    /**
     * 根据用户ID查询用户信息
     */

    Users queryUserInfo(String userId);

    /**
     * 对于用户信息进行更新并返回
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 对于用户的头像进行更新并返回用户的信息
     */
    Users updateUserFace(String userId, String faceUrl);
}
