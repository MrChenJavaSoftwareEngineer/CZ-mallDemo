package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //后台添加商品目录
    @ApiOperation("后台添加商品目录")
    @PostMapping("/admin/category/add")
    public ApiRestResponse add(@Valid @RequestBody AddCategoryReq addCategoryReq) throws ImoocMallException {
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq,category);
        categoryService.add(category);
        return ApiRestResponse.success();
    }

    //后台修改商品目录
    @ApiOperation("后台修改商品目录")
    @PostMapping("/admin/category/update")
    public ApiRestResponse update(@Valid @RequestBody UpdateCategoryReq updateCategoryReq) throws ImoocMallException {
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq,category);
        categoryService.update(category);
        return ApiRestResponse.success();
    }

    //后台删除商品目录
    @ApiOperation("后台删除商品目录")
    @PostMapping("/admin/category/delete")
    public ApiRestResponse delete(@RequestParam Integer id) throws ImoocMallException {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    //后台查询商品列表
    @ApiOperation("后台查询商品列表")
    @PostMapping("/admin/category/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.list(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    //前台查询商品列表
    @ApiOperation("前台查询商品列表")
    @GetMapping("/category/list")
    public ApiRestResponse userList(){
         List<CategoryVO> categoryVOS = categoryService.userList(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
