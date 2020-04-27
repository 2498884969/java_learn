package com.imooc.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.service.FdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FdfsServiceImpl implements FdfsService {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Override
    public String upload(MultipartFile multipartFile, String fileExtName) throws Exception {


        StorePath storePath = fastFileStorageClient.uploadFile(
                multipartFile.getInputStream(),
                multipartFile.getSize(),
                fileExtName,
                null);


        return storePath.getFullPath();
    }
}
