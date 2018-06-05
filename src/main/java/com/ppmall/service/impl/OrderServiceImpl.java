package com.ppmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;

import com.alipay.api.request.AlipayTradePrecreateRequest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.CartMapper;
import com.ppmall.dao.OrderItemMapper;
import com.ppmall.dao.OrderMapper;
import com.ppmall.dao.ProductMapper;
import com.ppmall.dao.ShippingMapper;
import com.ppmall.pojo.Order;
import com.ppmall.pojo.OrderItem;
import com.ppmall.pojo.Product;
import com.ppmall.pojo.Shipping;
import com.ppmall.service.IOrderService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.vo.CartProductVo;
import com.ppmall.vo.OrderInfoVo;
import com.ppmall.vo.OrderItemVo;

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

    @Autowired
    private ProductMapper productMapper;

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

        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setCreateTime(DateUtil.getDateString(order.getCreateTime()));
        orderInfoVo.setImageHost(imageHost);
        orderInfoVo.setOrderItemVoList(orderItems);
        orderInfoVo.setOrderNo(order.getOrderNo());
        orderInfoVo.setPayment(order.getPayment());
        orderInfoVo.setPaymentTypeDesc(order.getPaymentType() != null ? Const.PayType.codeOf(order.getPaymentType()).getDesc() : "");
        orderInfoVo.setShippingVo(shipping);
        orderInfoVo.setStatus(order.getStatus());
        orderInfoVo.setStatusDesc(Const.OrderStatus.codeOf(order.getStatus()).getDesc());

        return ServerResponse.createSuccess("获取成功", orderInfoVo);
    }

    @Override
    public ServerResponse getOrderCart(int userId) {
        List<OrderItemVo> list = orderItemMapper.selectCart(userId, 1);
        double cartTotalPrice = 0;
        for (OrderItemVo vo : list) {
            double totalPrice = vo.getTotalPrice();
            cartTotalPrice += totalPrice;

        }

        Map returnMap = new HashMap<>();
        returnMap.put("imageHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));
        returnMap.put("productTotalPrice", cartTotalPrice);
        returnMap.put("orderItemVoList", list);
        return ServerResponse.createSuccess("获取成功", returnMap);
    }

    @Override
    @Transactional
    public ServerResponse createOrder(int userId, int shippingId) {
        List<CartProductVo> cartCheckedList = cartMapper.selectCartProductListByUserIdAndChecked(userId, 1);// 1是选中

        long orderNo = generateOrderNo();
        Date date = DateUtil.getDate();

        double paymentTotal = 0;

        List batchInsertList = new ArrayList();
        List deleteCartList = new ArrayList<>();
        List updateProductList = new ArrayList<>();

        if (cartCheckedList == null || cartCheckedList.size() == 0) {
            return ServerResponse.createErrorMessage("购物车为空~");
        }

        for (CartProductVo vo : cartCheckedList) {
            OrderItem item = new OrderItem();
            item.setOrderNo(orderNo);
            item.setCurrentUnitPrice(new BigDecimal(vo.getProductPrice()));
            item.setProductId(vo.getProductId());
            item.setProductImage(vo.getProductMainImage());
            item.setProductName(vo.getProductName());
            item.setQuantity(vo.getQuantity());
            item.setTotalPrice(new BigDecimal(vo.getProductTotalPrice()));
            paymentTotal += vo.getProductTotalPrice();
            batchInsertList.add(item);
            deleteCartList.add(vo.getProductId());
            Product product = new Product();
            product.setId(vo.getProductId());
            product.setStock(vo.getProductStock() - vo.getQuantity());
            updateProductList.add(product);
        }

        BigDecimal payment = new BigDecimal(paymentTotal);

        Order order = new Order();
        order.setCreateTime(date);
        order.setOrderNo(orderNo);
        order.setPayment(payment);
        order.setPostage(0);
        order.setShippingId(shippingId);
        order.setStatus(Const.OrderStatus.UNPAY.getCode());
        order.setUpdateTime(date);
        order.setUserId(userId);
        order.setOrderNo(orderNo);

        orderMapper.insertSelective(order);
        orderItemMapper.insertBatchSelective(batchInsertList);
        productMapper.updateBatchSelective(updateProductList);
        cleanCart(userId, deleteCartList);

        ServerResponse response = getOrderDetail(orderNo);
        return ServerResponse.createSuccess("创建成功", response.getData());
    }

    private int cleanCart(int userId, List deleteCartList) {
        Map paramMap = new HashMap<>();
        paramMap.put("productIds", deleteCartList.toArray());
        paramMap.put("userId", userId);
        return cartMapper.deleteByProductIds(paramMap);
    }

    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    @Override
    public ServerResponse getOrderList(int userId, int pageNum, int pageSize) {
        // TODO Auto-generated method stub
        PageHelper.startPage(pageNum, pageSize);
        Map paramMap = new HashMap();
        paramMap.put("userId", userId);
        List<Order> orderList = orderMapper.selectAll(paramMap);
        PageInfo<Order> pageResult = new PageInfo<>(orderList);
        return ServerResponse.createSuccess("获取成功", pageResult);

    }

    @Override
    public ServerResponse cancelOrder(Long orderNo) {
        // TODO Auto-generated method stub
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createErrorMessage("该用户没有此订单");
        }

        if (order.getStatus() != Const.OrderStatus.UNPAY.getCode()) {
            String desc = Const.OrderStatus.codeOf(Const.OrderStatus.UNPAY.getCode()).getDesc();
            return ServerResponse.createErrorMessage("订单状态为" + desc + ",不能取消");
        }
        order = new Order();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatus.CANCEL.getCode());
        order.setUpdateTime(DateUtil.getDate());
        orderMapper.updateByOrderNoSelective(order);
        return ServerResponse.createSuccessMessage("操作成功");
    }

    @Override
    public ServerResponse payForOrder(Long orderNo, String path) throws AlipayApiException, IOException {
        // TODO Auto-generated method stub
//        String gatewayUrl = "https://openapi.alipaydev.com/gateway.do"; // AliPayConfig.getConfigValue("mcloud_api_domain");
//        String app_id = "2016091400509292";
//        String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZQN7dI+JglnYIAnfQ7QxNfhy8B6vkLU6eY4qJ02AYwH3Fi9spuwi2R6pbWcOUie0FcQhlpIdEr/zwhNUSe7xz8z3GOTl8RCbMzWjBM/uPtWlC4icIDQCtT2hum5RE6Yq3wQHDliSz/Wt+5bqIbeqSfXsy712XNft9JP75WSajo/Fqr/byNhujsT96gzG0ZV6VdTtBG950UpKuQbJGCW8gMJR8U5Fm4tyMt1wGBT1Yz4739RFbvv5sGaYRdRzLhMM0Vx/KGFpU1qfoVzDa6U5ERlUTXUO4ylPJzRtsymbPZ8VtyKV6YGIAPrvxY3Lc6GGb8BHRtqwHgl4Kr+CTRYAVAgMBAAECggEAQ2mBnwzV217T9JoBUmmza7L5uMw3FFvJpWpr2kycjMa/jFIEycp3/pZvnVdS7Nfu5uHdq7g/uDshrDsB7ut27holJjitzLe9yYDhf3r6QTCvaLhKKwRtM88mROEyy01fs4y21e4JnxLuYhdzgee3s1B3DOS16nnYcif/8HcCxB//I9GqkxWSWzps+ohGBnH/qXNWvw1CIiYH4F6HOWOJU8hnURxQiWx6bqkTnIpoj8EVQVaDohvz6n9FLUyWvO8crDI2U2eRn0AHXd9js2SQdF2Ie0hvnqlvLbZmcl2tOkjp5mNlFoz9SYVmH4ux0i3sbwUYg5m+cqUO13jHfBongQKBgQDGurvTOIVD0Tih38TA5VlfCcCZE5gUMzZEO5m5KpQzej1tvTQDX7Xgq/0EsMlIQXJFARxJRIcv/2G3aaPwahWipzlwMietwQVth29h2VLduvfGzC9IUbn00n9cW5dLCspQ623W/J5V7uDW+6oLZM50po5CcxVza04kWpijJdASPQKBgQDFayRtSGCfsRwnD/PAtPlm6rKbaFZrT9mvwRCxUew0TSrbNybttMsf8HfTW6VEdifJCXY8177f80eC2BwobJtWKJnzmhcQMFgBz+t0DDTCFZjkzM0ybsM9DRBmb3ipiGNOOUbHA7mPHjyvdPHDjvqk16xEKBW/VoQiari6T8i6uQKBgDHsl2LowBHZbbC81mDfPSRy/r91/K6WbvuVPXkXCUQlYWlALuDqGnbmvhl/kBFm354WOVTuskeMkK/TCixLekPyXqug7fbolsR9Ua3zOq4ZWkXG5dn1LhIjD3vURp3DiC/r9RwdOmm8KR/Y+U8DdBo5/WMUJj9opajcWZwi07LBAoGBAIDfhnIIk3rrxTMCyN59xWmXwGyO2gtHnxWKdPksP4OM9HMSN8lpAkihU7eX1fUxJJuqH5NsS2Aqkf4qUYdaDrVZ39YUOwYIaQsVzeB9r1sbeimyQCmyAKW0B4a3Mg+JxznjTf7QwatZ08e+EazVg21klUIQ4NF9Ctjkzh5hwumRAoGBAL+gRG1Dy5E1d4z307PjRpdLwIJIEoOtWjAygQnXxyTv/2krCgT85Y9EecqWcVt4uSqYXb3bbCwmWKnKmVksvxUk66XdzU4JJ+P5hijwRUfdLN/I8PcpIpyRA1gqknErdfDhSPEW8b2pQL3npU79hMlOwNJXf1tHU/l3C3opvSp9";
//        String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApaFBrbsOSR/MGK5ZWrBH/eQQ9lEMeH1CKx5WQSFphBDNHE3k7SD0ItqQ2yRgXKpxI34UjMAGAwoD1Z1sIlRtmCK9K1MGDW4D+WYgCqWaKWf9hOw60biVPBD3LQuJEx5/qBHgEMUs6mF1deMAZaRbUrE4mK8Os4dNVBwPHPZ8ZtL0kNIREmxfxCRmLwSVRbkNaPxGHe1NDC0vk8ppmWJSqUN430xCV0zoXmr/HJde+RfQ1a45+0raNM0XVqDp3JvUulrFeBmKnfoJEQhhu7mRhjNKJ5MODXG9A808Q7qVsVEZ26y6kY96ZxC4W3/3TAK1VSpcw0QXLKXMq2n/k2wENQIDAQAB";
//        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, merchant_private_key, "json", "utf-8",
//                alipay_public_key, "RSA2"); // 获得初始化的AlipayClient

        // 查询相关订单信息

        OrderInfoVo orderInfoVo = (OrderInfoVo) getOrderDetail(orderNo).getData();

        // 设置请求参数
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"));

        String out_trade_no = String.valueOf(orderInfoVo.getOrderNo());
        String total_amount = String.valueOf(orderInfoVo.getPayment());
        String discountable_amount = "0";
        String body = new StringBuilder().append("订单").append(out_trade_no).append("购买商品共").append(total_amount).append("元").toString();
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";



        ObjectMapper objectMapper = new ObjectMapper();

        File qrFile = new File(path);
        if (!qrFile.exists()) {
            qrFile.setWritable(true);
            qrFile.mkdirs();
        }
        String qrFileName = "qr_" + orderNo +".png";
        String qrPath = path + qrFileName;


        //ZxingUtil.getQRCodeImge(qrCode, 256, qrPath);

        return ServerResponse.createSuccess("");
    }
}
