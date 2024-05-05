package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.vo.OrderStatisticsVO;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class OrderAdminController {
    @Autowired
    OrderService orderService;

    //后台订单的查询
    @ApiOperation("后台订单的查询")
    @PostMapping("/order/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) throws ImoocMallException {
        PageInfo<OrderVO> list = orderService.list(pageNum, pageSize);
        return ApiRestResponse.success(list);
    }

    //后台订单的发送
    @ApiOperation("后台订单的发送")
    @PostMapping("/order/deliver")
    public ApiRestResponse deliver(@RequestParam String orderNo) throws ImoocMallException {
        orderService.deliver(orderNo);
        return ApiRestResponse.success();
    }

    //后台订单的完结
    @ApiOperation("后台订单的完结")
    @PostMapping("/order/finish")
    public ApiRestResponse finish(@RequestParam String orderNo) throws ImoocMallException {
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台进行统计一段时间内的订单数")
    @PostMapping("/order/statistics")
    public ApiRestResponse statistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")Date start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")Date end){
        List<OrderStatisticsVO> statistics = orderService.statistics(start, end);
        return ApiRestResponse.success(statistics);
    }
}


