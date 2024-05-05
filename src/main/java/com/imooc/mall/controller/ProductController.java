package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    //前台商品详情
    @ApiOperation("前台商品详情")
    @GetMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
         Product detail = productService.detail(id);
        return ApiRestResponse.success(detail);
    }

    //前台进行商品搜寻
    @ApiOperation("前台进行商品搜寻")
    @GetMapping("/product/list")
    public ApiRestResponse list(ProductListReq productListReq){
//        PageInfo<Product> productPageInfo = productService.listOfUser(productListReq);
        PageInfo<Product> productPageInfo = productService.listOfUser(productListReq);
        return ApiRestResponse.success(productPageInfo);
    }
}
