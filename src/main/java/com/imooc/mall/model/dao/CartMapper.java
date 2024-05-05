package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Cart;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> selectList(Integer userId);

    Cart selectByProductIdAndUserId(@Param("userId") Integer userId,
                                    @Param("productId") Integer productId);

    Integer selectOrNot(@Param("userId") Integer userId,
                        @Param("productId") Integer productId,@Param("selected") Integer selected);
}