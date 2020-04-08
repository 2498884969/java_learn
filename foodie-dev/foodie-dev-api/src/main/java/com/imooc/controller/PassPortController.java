package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Api(value = "用户登录注册", tags = {"用于用户登录注册的相关接口"})
@RestController
@RequestMapping("passport")
public class PassPortController extends BaseController{

    @Autowired
    UserService userService;



    @ApiOperation(value = "判断用户是否存在", notes = "判断用户是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username ){

        // 1. 空值判断
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名为空");
        }

        // 2. 查找注册的用户是否存在
        boolean isExist = userService.queryUsernameIsExist(username);

        if (isExist){
            return IMOOCJSONResult.errorMsg("用户已经存在");
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPWD = userBO.getConfirmPassword();

        // 0.判断用户名和密码不能为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPWD)) {
            return IMOOCJSONResult.errorMsg("用户名或者密码为空");
        }

        // 1.查询用户名是否存在
        if (userService.queryUsernameIsExist(username)) {
            return IMOOCJSONResult.errorMsg("当前用户名已经存在");
        }

        // 2.密码长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能小于6");
        }

        // 3.判断两次密码是否一致
        if (!password.equals(confirmPWD)) {
            return IMOOCJSONResult.errorMsg("两次密码不一致");
        }

        // 4.实现注册
        Users userResult = userService.createUser(userBO);

        UsersVO usersVO = convertUserVO(userResult);

        CookieUtils.setCookie(request, response,"user", JsonUtils.objectToJson(usersVO), true);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0.判断用户名和密码不能为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或者密码为空");
        }
        // 1. 查询出要登录的用户
        Users result = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        UsersVO usersVO = convertUserVO(result);
        // 4. 复制属性设置cookie
        CookieUtils.setCookie(request, response,"user", JsonUtils.objectToJson(usersVO), true);
        // 5. 同步购物车数据
        syncShopCart(request, response, result.getId());

        return IMOOCJSONResult.ok(result);

    }



    /**
     * 1. 如果redis中的数据为空，购物车中也为空，不处理
     *                          购物车中不为空，以购物车为基准
     * 2. 如果redis中的数据不为空，购物车中为空，以redis为准
     *                          购物车不为空，相同产品以购物车中的为准--参考京东
     * 3. 同步到redis中去了以后，覆盖本地cookie中购物车的数据保证购物车是最新的数据
     */
    private void syncShopCart(HttpServletRequest request,
                              HttpServletResponse response,
                              String userId) {

        String key = FOODIE_SHOPCART + ":" + userId;
        // 1.redis中的数据
        String redisShopCartStr = redisOperator.get(key);
        // 2.cookie中的数据
        String cookieShopCartStr =
                CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(redisShopCartStr)) {
            if (StringUtils.isNotBlank(cookieShopCartStr)){
                redisOperator.set(key, cookieShopCartStr);
            }
        } else {
            if (StringUtils.isNotBlank(cookieShopCartStr)) {
                // 1.cookie的数据会覆盖redis中的数据，然后两个取并集
                // redis
                List<ShopcartBO> shopcartBOListRedis = JsonUtils.jsonToList(redisShopCartStr, ShopcartBO.class);
                // cookie
                List<ShopcartBO> shopcartBOListCookie = JsonUtils.jsonToList(cookieShopCartStr, ShopcartBO.class);
                List<ShopcartBO> pendingRemovedList = new ArrayList<>();
                for (ShopcartBO scbReids: shopcartBOListRedis){
                    for (ShopcartBO scbCookie: shopcartBOListCookie) {
                        // 重写
                        if (scbReids.getSpecId().equals(scbCookie.getSpecId())){
                            scbReids.setBuyCounts(scbReids.getBuyCounts());
                            pendingRemovedList.add(scbCookie);
                        }
                    }
                }
                shopcartBOListCookie.removeAll(pendingRemovedList);
                shopcartBOListRedis.addAll(shopcartBOListCookie);
                redisOperator.set(key, JsonUtils.objectToJson(shopcartBOListRedis));
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART,
                        JsonUtils.objectToJson(shopcartBOListRedis), true);

            } else {
                CookieUtils.setCookie(request,
                        response, FOODIE_SHOPCART, redisShopCartStr, true);
            }
        }

    }


    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 1. 删除包含有用户信息的cookie
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        CookieUtils.deleteCookie(request, response, "user");
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);

        // 2. 清空会话

        return IMOOCJSONResult.ok();

    }


}
