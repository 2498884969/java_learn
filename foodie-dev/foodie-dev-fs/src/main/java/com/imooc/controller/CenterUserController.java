package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.resource.FileResource;
import com.imooc.service.FdfsService;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/fdfs")
public class CenterUserController extends BaseController {

    @Autowired
    CenterUserService centerUserService;

    @Autowired
    FdfsService fdfsService;

    @Autowired
    FileResource fileResource;


    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(String userId,
                                      MultipartFile file,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception{


        if (file == null) {
            return IMOOCJSONResult.errorMsg("上传的图片不能为空");
        }

        String[] strings = file.getOriginalFilename().split("\\.");
        String suffix = strings[strings.length-1];

        // 这里需要对于上传的文件格式进行校验，防止ajax攻击(通过上传一些js、sh脚本)
        if(!suffix.equalsIgnoreCase("png") &&
                !suffix.equalsIgnoreCase("jpg") &&
                !suffix.equalsIgnoreCase("jpeg")) {
            return IMOOCJSONResult.errorMsg("上传的图片格式不支持");
        }

        String path = fdfsService.upload(file, suffix);
        System.out.println(path);

        if (StringUtils.isNotBlank(path)) {
            String finalUserFaceUrl = fileResource.getHost() + path;

            Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

            UsersVO usersVO = convertUserVO(userResult);

            CookieUtils.setCookie(request, response, "user",
                    JsonUtils.objectToJson(usersVO), 24*3600,true);
        } else {
            return IMOOCJSONResult.errorMsg("上传头像失败");
        }

        return IMOOCJSONResult.ok();
    }

}
