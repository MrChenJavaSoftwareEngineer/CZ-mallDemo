package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.OrderItemMapper;
import com.imooc.mall.model.dao.OrderMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.*;
import com.imooc.mall.model.query.OrderStatisticsQuery;
import com.imooc.mall.model.vo.OrderItemVO;
import com.imooc.mall.model.vo.OrderStatisticsVO;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.OrderService;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.OrderCodeFactory;
import com.imooc.mall.util.QRCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

//    @Value("${file.upload.ip}")
//    String ip;

    String ui="127.0.0.1:8081";


    @Override
    public PageInfo<OrderVO> list(Integer pageNum, Integer pageSize) throws ImoocMallException {
        List<Order> orders = orderMapper.selectByUserId(UserFilter.currentUser.getId());
        if (orders == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ORDER);
        }

        PageHelper.startPage(pageNum,pageSize);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (!Constant.OrderStatus.CANCEL.equals(order.getOrderStatus())) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                orderVOList.add(orderVO);
            }
        }
        return new PageInfo<>(orderVOList);
    }

    @Override
    public String qrCode(String orderNo) throws ImoocMallException, IOException, WriterException {
         Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ORDER);
        }
//         ServletRequestAttributes requestAttributes =
//                 (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//         HttpServletRequest request = requestAttributes.getRequest();
         String address= ui;
         String payUrl="http://"+address+"/user"+"/pay?orderNo="+orderNo;
        if (order.getOrderStatus().equals(Constant.OrderStatus.NOT_PAY.getStatusCode())){
            QRCodeGenerator.generateQRCodeImage(
                    payUrl,350,400,Constant.FILE_UPLOAD_DIR+orderNo+".png"
            );
        }else {
            throw new ImoocMallException(ImoocMallExceptionEnum.QR_WRONG_ORDER_STATUS);
        }
        String qrCodeQrAddress="http://"+address+"/images/"+orderNo+".png";
        return qrCodeQrAddress;
    }

    @Override
    public OrderVO detail(String orderNo) throws ImoocMallException {
         Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ORDER);
        }
        List<OrderItemVO> orderItems = new ArrayList<>();
        for (OrderItem orderItem : orderItemMapper.selectByOrderNo(orderNo)) {
             OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItems.add(orderItemVO);
        }
         OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        orderVO.setOrderItemVOList(orderItems);
        return orderVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createOrder(Order order) throws ImoocMallException {
        //进行订单的基础准备,如orderNo，totalPrice等
        //要进行mysql的中的数据值类型要和后端统一才行。
        String orderNo = OrderCodeFactory.getOrderCode(UserFilter.currentUser.getId().longValue());
        Integer totalPrice= 0;
        Integer price=0;
        //过滤没有打钩的购物车
        List<Cart> cartlist = new ArrayList<>();
        for (Cart cart : cartMapper.selectList(UserFilter.currentUser.getId())) {
            if (cart.getSelected().equals(Constant.CartSelected.CART_SELECTED.getSelectedCode())){
                cartlist.add(cart);
            }
        }
        //检验商品的合法性
        //减商品的库存
        //减去已勾选的购物车
        //遍历写入Item表
        for (Cart carts:
             cartlist) {
            cartService.validProduct(carts.getProductId(),carts.getQuantity());
             Product product = productMapper.selectByPrimaryKey(carts.getProductId());
            totalPrice+= carts.getQuantity() * product.getPrice();
            price=totalPrice;
            int count = product.getStock() - carts.getQuantity();
             product.setStock(count);
             int i = productMapper.updateByPrimaryKeySelective(product);
            if (i == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
            }
            int in = cartMapper.deleteByPrimaryKey(carts.getId());
            if (in == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNo);
            orderItem.setProductId(carts.getProductId());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setProductImg(product.getImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(price);
            orderItem.setQuantity(carts.getQuantity());
            int i1 = orderItemMapper.insertSelective(orderItem);
            if (i1 == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
            }
        }
        //生成订单和写入order表
        order.setOrderNo(orderNo);
        order.setUserId(UserFilter.currentUser.getId());
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(Constant.OrderStatus.NOT_PAY.getStatusCode());
         int i = orderMapper.insertSelective(order);
        if (i == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
        return orderNo;
    }

    @Override
    public void pay(String orderNo) throws ImoocMallException {
         Order result = orderMapper.selectByOrderNo(orderNo);
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ORDER);
        }
        if (!Constant.OrderStatus.PAY.equals(result.getOrderStatus())){
            throw new ImoocMallException(ImoocMallExceptionEnum.PAY_WRONG_ORDER_STATUS);
        }
        result.setOrderStatus(Constant.OrderStatus.PAY.getStatusCode());
         int i = orderMapper.updateByPrimaryKeySelective(result);
        if (i == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void cancel(String orderNo) throws ImoocMallException {
        Order result = orderMapper.selectByOrderNo(orderNo);
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ORDER);
        }
        if (!userService.adminCheck(UserFilter.currentUser)&&
                !UserFilter.currentUser.getId().equals(result.getUserId())
                ||result.getOrderStatus().equals(Constant.OrderStatus.DELIVER.getStatusCode())){
            throw new ImoocMallException(ImoocMallExceptionEnum.CANCEL_WRONG_ORDER_STATUS);
        }
        result.setOrderStatus(Constant.OrderStatus.CANCEL.getStatusCode());
        int i = orderMapper.updateByPrimaryKeySelective(result);
        if (i == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void finish(String orderNo) throws ImoocMallException {
        Order result = orderMapper.selectByOrderNo(orderNo);
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ORDER);
        }
        if (!Constant.OrderStatus.FINISH.equals(result.getOrderStatus())){
            throw new ImoocMallException(ImoocMallExceptionEnum.FINISH_WRONG_ORDER_STATUS);
        }
        result.setOrderStatus(Constant.OrderStatus.FINISH.getStatusCode());
        int i = orderMapper.updateByPrimaryKeySelective(result);
        if (i == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void deliver(String orderNo) throws ImoocMallException {
        Order result = orderMapper.selectByOrderNo(orderNo);
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ORDER);
        }
        if (!Constant.OrderStatus.DELIVER.equals(result.getOrderStatus())){
            throw new ImoocMallException(ImoocMallExceptionEnum.DELIVER_WRONG_ORDER_STATUS);
        }
        result.setOrderStatus(Constant.OrderStatus.DELIVER.getStatusCode());
        int i = orderMapper.updateByPrimaryKeySelective(result);
        if (i == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public List<OrderStatisticsVO> statistics(Date start, Date end) {
        //进行Query的属性的赋值
        OrderStatisticsQuery orderStatisticsQuery = new OrderStatisticsQuery();
        orderStatisticsQuery.setStart(start);
        orderStatisticsQuery.setEnd(end);
        //进行数据库的查询
        List<OrderStatisticsVO> orderStatisticsVOS = orderMapper.selectOrderStatistics(orderStatisticsQuery);
        return orderStatisticsVOS;
    }
}
