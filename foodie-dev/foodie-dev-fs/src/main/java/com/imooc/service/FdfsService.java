package com.imooc.service;

import org.springframework.web.multipart.MultipartFile;

public interface FdfsService {

    String upload(MultipartFile multipartFile, String fileExtName) throws Exception;

}
