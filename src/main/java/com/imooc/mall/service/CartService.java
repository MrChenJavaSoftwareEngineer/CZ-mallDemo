package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.vo.CartVO;

import java.util.List;

public interface CartService {

    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer id, Integer productId, Integer count) throws ImoocMallException;

    void validProduct(Integer productId, Integer count) throws ImoocMallException;

    List<CartVO> update(Integer id, Integer productId, Integer count) throws ImoocMallException;

    List<CartVO> selectOrNot(Integer id, Integer productId, Integer selected) throws ImoocMallException;

    List<CartVO> selectAll(Integer id, Integer selected) throws ImoocMallException;
}
