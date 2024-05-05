package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.query.OrderStatisticsQuery;
import com.imooc.mall.model.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByUserId(Integer userId);

    Order selectByOrderNo(String orderNo);

    List<OrderStatisticsVO> selectOrderStatistics(@Param("query") OrderStatisticsQuery orderStatisticsQuery);
}