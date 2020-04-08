package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "center-user-userInfo相关接口", tags = {"center-user-userInfo相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    CenterUserService centerUserService;

    @Autowired
    FileUpload fileUpload;

    @ApiOperation(value = "更新用户数据并保存到cookie中", notes = "更新用户数据并保存到cookie中", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return IMOOCJSONResult.errorMap(getErrors(bindingResult));
        }

        // 1.根据userId对于数据库中的数据进行更新
        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        // 2. 消除关键字段将最新的用户数据写入cookie之中

        UsersVO usersVO = convertUserVO(users);
        // 后续要改，增加令牌token，会整合Redis 分布式会话, 要在更新的cookie里面添加token
        CookieUtils.setCookie(request, response,"user", JsonUtils.objectToJson(usersVO), true);

        return IMOOCJSONResult.ok();

    }

    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        // 后面调整
//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        String uploadPathPrefix = File.separator + userId;

        String[] strings = file.getOriginalFilename().split("\\.");
        String suffix = strings[strings.length-1];

        // 这里需要对于上传的文件格式进行校验，防止ajax攻击(通过上传一些js、sh脚本)
        if(!suffix.equalsIgnoreCase("png") &&
                !suffix.equalsIgnoreCase("jpg") &&
                !suffix.equalsIgnoreCase("jpeg")) {
            return IMOOCJSONResult.errorMsg("上传的图片格式不支持");
        }

        String newFileName = "face-" + userId + "." + suffix;
        String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

        File outFile = new File(finalFacePath);

        if (outFile.getParentFile() != null) {
            // 创建文件夹
            boolean res = outFile.getParentFile().mkdirs();
            System.out.println(res);
        }

        FileOutputStream fileOutputStream = null;
        // 拷贝文件内容通过流
        try {
            fileOutputStream = new FileOutputStream(outFile);
            InputStream inputStream = file.getInputStream();
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 由于浏览器可能存在缓存所以需要在这里加上时间戳以保证及时刷新
        String faceUrl = fileUpload.getImageServerUrl() + userId + "/"+ newFileName + "?t=" +
        DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        Users users = centerUserService.updateUserFace(userId, faceUrl);
        // 后续要改，增加令牌token，会整合Redis 分布式会话, 要在更新的cookie里面添加token
        UsersVO usersVO = convertUserVO(users);
        CookieUtils.setCookie(request, response,"user", JsonUtils.objectToJson(usersVO), true);

        return IMOOCJSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error: errorList) {
            map.put(error.getField(), error.getDefaultMessage());
        }
        return map;
    }

}
