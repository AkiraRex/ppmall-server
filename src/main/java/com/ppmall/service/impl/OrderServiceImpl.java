package com.ppmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.CartMapper;
import com.ppmall.dao.OrderItemMapper;
import com.ppmall.dao.OrderMapper;
import com.ppmall.dao.ShippingMapper;
import com.ppmall.pojo.Order;
import com.ppmall.pojo.OrderItem;
import com.ppmall.pojo.Shipping;
import com.ppmall.service.IOrderService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.util.UUIDUtil;
import com.ppmall.vo.CartProductVo;
import com.ppmall.vo.OrderItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public ServerResponse getOrderList(Long orderNum, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Map paramMap = new HashMap();
        paramMap.put("orderNum", orderNum);
        List<Order> orderList = orderMapper.selectAll(paramMap);
        PageInfo<Order> pageResult = new PageInfo<>(orderList);
        return ServerResponse.createSuccess("获取成功", pageResult);

    }

    @Override
    public ServerResponse getOrderDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null)
            return ServerResponse.createErrorMessage("找不到该订单");

        int shippingId = order.getShippingId();
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(orderNo);
        String imageHost = PropertiesUtil.getProperty("ftp.server.http.prefix");


        Map orderInfo = new HashMap();
        orderInfo.put("orderNo", order.getOrderNo());
        orderInfo.put("createTime", DateUtil.getDateString(order.getCreateTime()));
        orderInfo.put("status", order.getStatus());
        orderInfo.put("statusDesc", Const.OrderStatus.codeOf(order.getStatus()).getDesc());
        orderInfo.put("paymentTypeDesc", Const.PayType.codeOf(order.getPaymentType()).getDesc());
        orderInfo.put("payment", order.getPayment());
        orderInfo.put("shippingVo", shipping);
        orderInfo.put("orderItemVoList", orderItems);
        orderInfo.put("imageHost", imageHost);

        return ServerResponse.createSuccess("获取成功", orderInfo);
    }

    @Override
    public ServerResponse getOrderCart(int userId) {
        List<OrderItemVo> list = orderItemMapper.selectCart(userId,1);
        double cartTotalPrice = 0;
        for (OrderItemVo vo : list) {
            double totalPrice = vo.getTotalPrice();
            cartTotalPrice += totalPrice;

        }

        Map returnMap = new HashMap<>();
        returnMap.put("imageHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));
        returnMap.put("productTotalPrice", cartTotalPrice);
        returnMap.put("orderItemVoList", list);
        return ServerResponse.createSuccess("获取成功",returnMap);
    }

    @Override
    @Transactional
    public ServerResponse createOrder(int userId) {
        List<CartProductVo> cartCheckedList = cartMapper.selectCartProductListByUserIdAndChecked(userId,1);
        //long orderNo = UUIDUtil.getUUID();
        Order order = new Order();
        for (CartProductVo vo: cartCheckedList) {
            OrderItem item = new OrderItem();
            item.setOrderNo(32266666l);
        }

        return null;
    }

}
