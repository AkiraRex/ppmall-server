package com.ppmall.service.impl;

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
import com.ppmall.vo.OrderItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

		Map orderInfo = new HashMap();
		orderInfo.put("orderNo", order.getOrderNo());
		orderInfo.put("createTime", DateUtil.getDateString(order.getCreateTime()));
		orderInfo.put("status", order.getStatus());
		orderInfo.put("statusDesc", Const.OrderStatus.codeOf(order.getStatus()).getDesc());
		orderInfo.put("paymentTypeDesc",
				order.getPaymentType() != null ? Const.PayType.codeOf(order.getPaymentType()).getDesc() : "");
		orderInfo.put("payment", order.getPayment());
		orderInfo.put("shippingVo", shipping);
		orderInfo.put("orderItemVoList", orderItems);
		orderInfo.put("imageHost", imageHost);

		return ServerResponse.createSuccess("获取成功", orderInfo);
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
		List<CartProductVo> cartCheckedList = cartMapper.selectCartProductListByUserIdAndChecked(userId, 1);

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
}
