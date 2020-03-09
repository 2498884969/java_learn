package com.imooc.exception;


import com.imooc.utils.IMOOCJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

// FileUploadBase$SizeLimitExceededException

@RestControllerAdvice
public class CustomExceptionHandler {

//    @ExceptionHandler(FileUploadBase.SizeLimitExceededException.class)
//    public IMOOCJSONResult handleMaxUploadFile(FileUploadBase.SizeLimitExceededException ex){
//        return IMOOCJSONResult.errorMsg("上传的最大文件不能超过500k");
//    }


    // 上传文件超过500k，捕获异常：MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public IMOOCJSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return IMOOCJSONResult.errorMsg("文件上传大小不能超过500k，请压缩图片或者降低图片质量再上传！");
    }
}
