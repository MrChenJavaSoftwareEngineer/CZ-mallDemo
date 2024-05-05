package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.vo.CategoryVO;

import java.util.ArrayList;
import java.util.List;

public interface CategoryService {
    void add(Category category) throws ImoocMallException;

    void update(Category category) throws ImoocMallException;

    void delete(Integer id) throws ImoocMallException;

    PageInfo list(Integer pageNum, Integer pageSize);



    List<CategoryVO> userList(Integer parentId);
}
