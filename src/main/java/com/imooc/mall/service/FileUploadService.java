package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileUploadService {
    void createFile(File fileDirectory, File destFile) throws ImoocMallException;

    void fileUploadOfExcel(MultipartFile file) throws IOException, ImoocMallException;

    //进行图片的水印的加印
    String getString(MultipartFile file) throws IOException, ImoocMallException;

    //进行图片的上传
    String getResult(MultipartFile file) throws IOException, ImoocMallException;
}
