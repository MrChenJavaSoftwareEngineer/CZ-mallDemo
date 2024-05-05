package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
public class CartController {
    @Autowired
    CartService cartService;

    //购物车列表
    @ApiOperation("购物车列表")
    @PostMapping("/list")
    public ApiRestResponse list(){
         List<CartVO> list = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(list);
    }

    //购物车添加
    @ApiOperation("购物车添加")
    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId,
                               @RequestParam Integer count) throws ImoocMallException {
         List<CartVO> add = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(add);
    }

    //购物车修改
    @ApiOperation("购物车修改")
    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId,
                                  @RequestParam Integer count) throws ImoocMallException {
        List<CartVO> update = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(update);
    }

    //选择购物车
    @ApiOperation("选择购物车")
    @PostMapping("/select")
    public ApiRestResponse select(@RequestParam Integer productId,
                                  @RequestParam Integer selected) throws ImoocMallException {
        List<CartVO> cartVOS = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestResponse.success(cartVOS);
    }

    //选择所有的购物车
    @ApiOperation("选择所有的购物车")
    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam Integer selected) throws ImoocMallException {
        List<CartVO> cartVOS = cartService.selectAll(UserFilter.currentUser.getId(), selected);
        return ApiRestResponse.success(cartVOS);
    }
}
