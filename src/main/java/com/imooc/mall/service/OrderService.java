package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.vo.OrderStatisticsVO;
import com.imooc.mall.model.vo.OrderVO;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface OrderService {
    PageInfo<OrderVO> list(Integer pageNum, Integer pageSize) throws ImoocMallException;

    String qrCode(String orderNo) throws ImoocMallException, IOException, WriterException;

    OrderVO detail(String orderNo) throws ImoocMallException;

    String createOrder(Order order) throws ImoocMallException;

    void pay(String orderNo) throws ImoocMallException;

    void cancel(String orderNo) throws ImoocMallException;

    void finish(String orderNo) throws ImoocMallException;

    void deliver(String orderNo) throws ImoocMallException;

    List<OrderStatisticsVO> statistics(Date start, Date end);
}
