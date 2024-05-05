package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.common.ValidList;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.FileUploadService;
import com.imooc.mall.service.ProductService;
import java.net.URI;

import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
//import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.createFile;

@RestController
@Validated
public class ProductAdminController {
    @Autowired
    ProductService productService;
    @Autowired
    FileUploadService fileUploadService;



    //后台增加商品
    @ApiOperation("后台增加商品")
    @PostMapping("/admin/product/add")
    public ApiRestResponse add(@Valid @RequestBody AddProductReq addProductReq) throws ImoocMallException {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        productService.add(product);
        return ApiRestResponse.success();
    }

    //后台进行文件的上传
    @ApiOperation("后台进行文件的上传")
    @PostMapping("/admin/upload/file")
    public ApiRestResponse file(/*HttpServletRequest httpServletRequest,*/
                                @RequestParam("file") MultipartFile file) throws ImoocMallException, IOException {
        String result = fileUploadService.getResult(file);
        return ApiRestResponse.success(result);
    }



//    //获得新的URI,便于重写URL
//    public URI getHost(URI uri) {
//        URI effectUri;
//        try {
//            effectUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
//                    null, null, null);
//        }  catch (URISyntaxException e) {
//          effectUri=null;
//        }
//        return effectUri;
//    }

    //商品的删除
    @ApiOperation("商品的删除")
    @PostMapping("/admin/product/delete")
    public ApiRestResponse delete(@RequestParam Integer id) throws ImoocMallException {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    //商品的更新
    @ApiOperation("商品的更新")
    @PostMapping("/admin/product/update")
    public ApiRestResponse update(@Valid @RequestBody UpdateProductReq updateProductReq) throws ImoocMallException {
         Product product = new Product();
         BeanUtils.copyProperties(updateProductReq,product);
         productService.update(product);
         return ApiRestResponse.success();
    }

    //后台的商品表
    @ApiOperation("后台的商品表")
    @PostMapping("/admin/product/listOfAdmin")
    public ApiRestResponse listOfAdmin(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize){
        PageInfo<Product> pageInfo = productService.list(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    //商品批量进行上下架
    @ApiOperation("商品批量进行上下架")
    @PostMapping("/admin/product/batchApdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,
                                                 @RequestParam Integer sellStatus) throws ImoocMallException {
        productService.batchUpdate(ids,sellStatus);
        return ApiRestResponse.success();
    }

    //后台Excel表批量上传商品
    @ApiOperation("后台Excel表批量上传商品")
    @PostMapping("/admin/product/uploadProduct")
    //excel上传文件，所以参数是文件类型
    public ApiRestResponse uploadProduct(@RequestParam("file") MultipartFile file) throws ImoocMallException, IOException {
        fileUploadService.fileUploadOfExcel(file);
        return ApiRestResponse.success();
    }


    //上传图片并缩放打水印
    @ApiOperation("上传图片并缩放打水印")
    @PostMapping("/admin/upload/imageFile")
    public ApiRestResponse imageFile(/*HttpServletRequest httpServletRequest,*/
                                @RequestParam("file") MultipartFile file) throws ImoocMallException, IOException {
        String result = fileUploadService.getString(file);
        return ApiRestResponse.success(result);
    }




    //商品的批量更新
    //注意：@valid进行检验时，若是批量的进行检验，那么@valid会失效的，单个则不会。
    //UpdateProductReq是javaBean中的，而valid是javautils中，所有不会进行校验。
    //validlist就是我们自己的javabean。
    /*
    解决法：1.手动解决：就是自己进行属性的if判断。属性比较多时就会比较麻烦了，低效。
          2.自定义列表 ValidList
          3.@Validated 增强版@Valid,1.具备对list集合进行验证（本次进行书写），
                                   2.具备了分组验证，若类中属性较多，可以进行部分验证（本次没有进行书写）
     */
    @ApiOperation("商品的批量更新")
    @PostMapping("/admin/product/batchUpdate")
    public ApiRestResponse batchUpdate(@Valid @RequestBody ValidList<UpdateProductReq> UpdateProductReqList) throws ImoocMallException {
        for (int i = 0; i < UpdateProductReqList.size(); i++) {
            UpdateProductReq updateProductReq = UpdateProductReqList.get(i);
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }

    @ApiOperation("商品的批量更新1")
    @PostMapping("/admin/product/batchUpdate1")
    public ApiRestResponse batchUpdate1(@Validated @RequestBody List<UpdateProductReq> UpdateProductReqList) throws ImoocMallException {
        for (int i = 0; i < UpdateProductReqList.size(); i++) {
            UpdateProductReq updateProductReq = UpdateProductReqList.get(i);
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }
}
