package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.imooc.mall.model.request.CreateOrderReq;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class OrderController {
    @Autowired
    OrderService orderService;

    //前台查询订单列表
    @ApiOperation("前台查询订单列表")
    @PostMapping("/order/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) throws ImoocMallException {
         PageInfo<OrderVO> list = orderService.list(pageNum, pageSize);
        return ApiRestResponse.success(list);
    }

    //前台创建支付二维码
    @ApiOperation("前台创建支付二维码")
    @PostMapping("/order/qrCode")
    public ApiRestResponse qrCode(@RequestParam String orderNo) throws ImoocMallException, IOException, WriterException {
        String s = orderService.qrCode(orderNo);
        return ApiRestResponse.success(s);
    }

    //前台订单详情查询
    @ApiOperation("前台订单详情查询")
    @PostMapping("/order/detail")
    public ApiRestResponse detail(@RequestParam String orderNo) throws ImoocMallException {
        OrderVO detail = orderService.detail(orderNo);
        return ApiRestResponse.success(detail);
    }

    //前台创建订单表
    @ApiOperation("前台创建订单表")
    @PostMapping("/order/createOrder")
    public ApiRestResponse createOrder(@Valid @RequestBody CreateOrderReq createOrderReq) throws ImoocMallException {
         Order order = new Order();
        BeanUtils.copyProperties(createOrderReq,order);
        String order1 = orderService.createOrder(order);
        return ApiRestResponse.success(order1);
    }

    //前台支付接口
    @ApiOperation("前台支付接口")
    @PostMapping("pay")
    public ApiRestResponse pay(@RequestParam String orderNo) throws ImoocMallException {
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

    //前台取消订单表
    @ApiOperation("前台取消订单表")
    @PostMapping("/order/cancel")
    public ApiRestResponse cancel(@RequestParam String orderNo) throws ImoocMallException {
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    //前台用户进行确认完成订单
    @ApiOperation("前台用户进行确认完成订单")
    @PostMapping("/order/finish")
    public ApiRestResponse finish(@RequestParam String orderNo) throws ImoocMallException {
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }
}
