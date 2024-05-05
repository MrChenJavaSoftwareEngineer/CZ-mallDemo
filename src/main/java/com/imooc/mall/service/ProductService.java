package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductListReq;

import java.io.File;
import java.io.IOException;

public interface ProductService {
    void add(Product product) throws ImoocMallException;

    void delete(Integer id) throws ImoocMallException;

    void update(Product product) throws ImoocMallException;

    PageInfo<Product> list(Integer pageNum, Integer pageSize);

    void batchUpdate(Integer[] ids, Integer sellStatus) throws ImoocMallException;

    Product detail(Integer id);


   PageInfo<Product> listOfUser(ProductListReq productListReq);


    PageInfo lists(ProductListReq productListReq);

    void addProductByExcel(File destFile) throws IOException, ImoocMallException;
}
