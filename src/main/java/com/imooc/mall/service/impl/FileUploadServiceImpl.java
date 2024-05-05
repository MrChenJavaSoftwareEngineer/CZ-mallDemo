package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.service.FileUploadService;
import com.imooc.mall.service.ProductService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    String ui="127.0.0.1:8081";
    @Autowired
    ProductService productService;
    @Override
    public void createFile(File fileDirectory, File destFile) throws ImoocMallException {
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.MAKE_FAILED);
            }
        }
        if (!destFile.exists()) {
            if (!destFile.mkdir()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.MAKE_FAILED);
            }
        }
    }

    @Override
    public void fileUploadOfExcel(MultipartFile file) throws IOException, ImoocMallException {
        //获取文件的类型后缀
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成uuid，避免文件名重复
        UUID uuid = UUID.randomUUID();
        String newFile = uuid + substring;
        //创建文件夹和文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile= new File(Constant.FILE_UPLOAD_DIR + newFile);
        createFile(fileDirectory, destFile);
        //进行文件的转移，必须是文件，不是字符串。
        file.transferTo(destFile);
        productService.addProductByExcel(destFile);
    }

    @Override
    public String getString(MultipartFile file) throws IOException, ImoocMallException {
        //截取文件后缀
        String originalFilename = file.getOriginalFilename();
        String sufixString = originalFilename.substring(originalFilename.lastIndexOf("."));
        //UUID生成文件名
        UUID uuid = UUID.randomUUID();
        //然后进行新的文件名的拼接
        String newFile = uuid + sufixString;
        //创建新的文件夹和文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFile);
        createFile(fileDirectory, destFile);
        /*
        把文件转移到新创建的文件中
        水印只能在已有的图片进行水印的打印
         */
        file.transferTo(destFile);
        //处理图片,并且把图片文件转移到新创建的图片文件中
        /*
        watermark表示打水印，
        第一个表示水印打在图片的那个的位置上
        第二个表示水印的地址
        ImageIO.read表示水印的读取
        第一个表示文件的路径
        第二个表示水印的透明度
        toFile
        表示对原来的图进行覆盖
         */
        Thumbnails.of(destFile).size(Constant.IMAGE_SIZE,Constant.IMAGE_SIZE)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read
                        (new File(Constant.FILE_UPLOAD_DIR+Constant.WATER_MARK_JPG)
                        ),Constant.IMAGE_OPACITY).toFile(new File(Constant.FILE_UPLOAD_DIR
                        +newFile));
//        //把文件转移到新创建的文件中
//        file.transferTo(destFile);
        //返回API
        String address=ui;
        String result = "http://" + address + "/images/" + newFile;
        return result;
    }
@Override
public String getResult(MultipartFile file) throws IOException, ImoocMallException {
        //截取文件后缀
        String originalFilename = file.getOriginalFilename();
        String sufixString = originalFilename.substring(originalFilename.lastIndexOf("."));
        //UUID生成文件名
        UUID uuid = UUID.randomUUID();
        //然后进行新的文件名的拼接
        String newFile = uuid + sufixString;
        //创建新的文件夹和文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFile);
        createFile(fileDirectory, destFile);
        //把文件转移到新创建的文件中
        file.transferTo(destFile);
        //返回API
        String address=ui;
        String result = "http://" + address + "/images/" + newFile;
        return result;
    }
}
