package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imooc.mall.common.Constant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CartMapper cartMapper;

    @Override
    public List<CartVO> list(Integer userId) {
        List<Cart> carts = cartMapper.selectList(userId);
        List<CartVO> cartVOS = listCartVO(carts);
        return cartVOS;
    }

    private List<CartVO> listCartVO(List<Cart> carts) {
        List<CartVO> cartVOList = new ArrayList<>();
        for (int i = 0; i < carts.size(); i++) {
            Cart cart = carts.get(i);
             CartVO cartVO = new CartVO();
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            BeanUtils.copyProperties(cart,cartVO);
            cartVO.setTotalPrice(product.getPrice()*cart.getQuantity());
            cartVO.setPrice(product.getPrice());
            cartVO.setProductImage(product.getImage());
            cartVO.setProductName(product.getName());
            cartVOList.add(cartVO);
        }
        return cartVOList;
    }


    @Override
    public List<CartVO> add(Integer id, Integer productId, Integer count) throws ImoocMallException {
        validProduct(productId,count);
         Cart result= cartMapper.selectByProductIdAndUserId(id, productId);
        if (result == null) {
             Cart cart = new Cart();
             cart.setQuantity(count);
             cart.setUserId(id);
             cart.setProductId(productId);
             int i = cartMapper.insertSelective(cart);
            if (i == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
            }
        }else{
             count= count + result.getQuantity();
             result.setQuantity(count);
             int i = cartMapper.updateByPrimaryKeySelective(result);
            if (i == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        }
        return this.list(id);
    }
    @Override
    public void validProduct(Integer productId, Integer count) throws ImoocMallException {
         Product result = productMapper.selectByPrimaryKey(productId);
        if (result == null||result.getStatus().equals(Constant.productStatus.NOT_SALE)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
        }
        if (count>result.getStock()){
            throw new ImoocMallException(ImoocMallExceptionEnum.LOWS_STOCK);
        }
    }

    @Override
    public List<CartVO> update(Integer id, Integer productId, Integer count) throws ImoocMallException {
        validProduct(productId,count);
         Cart result = cartMapper.selectByProductIdAndUserId(id, productId);
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_NOT_EXISTED);
        }
        result.setQuantity(count);
         int i = cartMapper.updateByPrimaryKeySelective(result);
        if (i == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
        return this.list(id);
    }

    @Override
    public List<CartVO> selectOrNot(Integer id, Integer productId, Integer selected) throws ImoocMallException {
         Integer count = cartMapper.selectOrNot(id, productId, selected);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
        return this.list(id);
    }

    //写完后在进行开发多选的接口

    @Override
    public List<CartVO> selectAll(Integer id, Integer selected) throws ImoocMallException {
         Integer count = cartMapper.selectOrNot(id, null, selected);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
        return this.list(id);
    }
}
