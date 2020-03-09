package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "用户登录注册", tags = {"用于用户登录注册的相关接口"})
@RestController
@RequestMapping("passport")
public class PassPortController {

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

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response,"user", JsonUtils.objectToJson(userResult), true);

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


        // 1.
        Users result = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        result = setNullProperty(result);

        CookieUtils.setCookie(request, response,"user", JsonUtils.objectToJson(result), true);

        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return IMOOCJSONResult.ok(result);

    }


    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 1. 删除包含有用户信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        return IMOOCJSONResult.ok();

    }

    private Users setNullProperty(Users userResult ) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);

        return userResult;
    }

}
