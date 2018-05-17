package com.ppmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.OrderItemMapper;
import com.ppmall.dao.OrderMapper;
import com.ppmall.dao.ShippingMapper;
import com.ppmall.pojo.Order;
import com.ppmall.pojo.OrderItem;
import com.ppmall.pojo.Shipping;
import com.ppmall.service.IOrderService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
