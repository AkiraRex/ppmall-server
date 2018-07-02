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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.config.Configs;
import com.alipay.model.ExtendParams;
import com.alipay.model.GoodsDetail;
import com.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.model.result.AlipayF2FPrecreateResult;
import com.alipay.service.AlipayTradeService;
import com.alipay.service.impl.AlipayTradeServiceImpl;
import com.alipay.util.ZxingUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.CartMapper;
import com.ppmall.dao.OrderItemMapper;
import com.ppmall.dao.OrderMapper;
import com.ppmall.dao.PayInfoMapper;
import com.ppmall.dao.ProductMapper;
import com.ppmall.dao.ShippingMapper;
import com.ppmall.pojo.Order;
import com.ppmall.pojo.OrderItem;
import com.ppmall.pojo.PayInfo;
import com.ppmall.pojo.Product;
import com.ppmall.pojo.Shipping;
import com.ppmall.service.IOrderService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.FtpUtil;
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

	@Autowired
	private PayInfoMapper payInfoMapper;

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
		orderInfoVo.setPaymentTypeDesc(
				order.getPaymentType() != null ? Const.PayType.codeOf(order.getPaymentType()).getDesc() : "");
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
			item.setUserId(userId);
			item.setCurrentUnitPrice(new BigDecimal(vo.getProductPrice()));
			item.setProductId(vo.getProductId());
			item.setProductImage(vo.getProductMainImage());
			item.setProductName(vo.getProductName());
			item.setQuantity(vo.getQuantity());
			item.setTotalPrice(new BigDecimal(vo.getProductTotalPrice()));
			item.setCreateTime(date);
			item.setUpdateTime(date);

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
		List returnList = new ArrayList<>();

		List<Order> orderList = orderMapper.selectAll(paramMap);
		for (Order order : orderList) {
			List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
			Shipping shippingVo = shippingMapper.selectByPrimaryKey(order.getShippingId());
			OrderInfoVo orderInfoVo = new OrderInfoVo();
			if (order.getCreateTime() != null)
				orderInfoVo.setCreateTime(DateUtil.getDateString(order.getCreateTime()));
			if (order.getPaymentTime() != null)
				orderInfoVo.setPaymentTime(DateUtil.getDateString(order.getPaymentTime()));
			if (order.getSendTime() != null)
				orderInfoVo.setSendTime(DateUtil.getDateString(order.getSendTime()));
			if (order.getCloseTime() != null)
				orderInfoVo.setCloseTime(DateUtil.getDateString(order.getCloseTime()));
			if (order.getEndTime() != null)
				orderInfoVo.setEndTime(DateUtil.getDateString(order.getEndTime()));
			if (order.getPaymentType() != null)
				orderInfoVo.setPaymentTypeDesc(Const.PayType.codeOf(order.getPaymentType()).getDesc());
			
			orderInfoVo.setStatus(order.getStatus());
			orderInfoVo.setStatusDesc(Const.OrderStatus.codeOf(order.getStatus()).getDesc());
			orderInfoVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
			orderInfoVo.setOrderItemVoList(orderItems);
			orderInfoVo.setOrderNo(order.getOrderNo());
			orderInfoVo.setPayment(order.getPayment());
			orderInfoVo.setShippingVo(shippingVo);
			returnList.add(orderInfoVo);
		}

		PageInfo pageResult = new PageInfo<>(returnList);
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
	public ServerResponse prepareOrder(Long orderNo) {
		// TODO Auto-generated method stub
		Order order = new Order();
		order.setOrderNo(orderNo);
		order.setUpdateTime(DateUtil.getDate());
		order.setStatus(Const.OrderStatus.PREPARE.getCode());
		orderMapper.updateByOrderNoSelective(order);
		return ServerResponse.createSuccessMessage("修改成功");
	}

	@Override
	public ServerResponse deliveryOrder(Long orderNo) {
		// TODO Auto-generated method stub
		Order order = new Order();
		Date now = DateUtil.getDate();
		order.setOrderNo(orderNo);
		order.setUpdateTime(now);
		order.setSendTime(now);
		order.setStatus(Const.OrderStatus.SHIPED.getCode());
		orderMapper.updateByOrderNoSelective(order);
		return ServerResponse.createSuccessMessage("修改成功");
	}

	@Override
	public ServerResponse payForOrder(Long orderNo, String path) throws AlipayApiException, IOException {
		// TODO Auto-generated method stub

		Configs.init("alipayConfig.properties");

		// 查询相关订单信息
		OrderInfoVo orderInfoVo = (OrderInfoVo) getOrderDetail(orderNo).getData();

		// 设置请求参数
		AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

		String outTradeNo = String.valueOf(orderInfoVo.getOrderNo());
		String totalAmount = String.valueOf(orderInfoVo.getPayment());
		String undiscountableAmount = totalAmount;
		String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount)
				.append("元").toString();
		// 商户操作员编号，添加此参数可以为商户操作员做销售统计
		String operatorId = "test_operator_id";
		// (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
		String storeId = "test_store_id";
		// 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
		ExtendParams extendParams = new ExtendParams();
		extendParams.setSysServiceProviderId("2088100200300400500");
		String timeoutExpress = "120m";
		// 商品明细列表，需填写购买商品详细信息，
		List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

		List<OrderItem> ordetItemList = orderInfoVo.getOrderItemVoList();

		for (OrderItem orderItem : ordetItemList) {
			GoodsDetail goods = GoodsDetail.newInstance(String.valueOf(orderItem.getProductId()),
					orderItem.getProductName(), orderItem.getTotalPrice().longValue() * 100, orderItem.getQuantity());
			goodsDetailList.add(goods);
		}

		AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder().setSubject("PPMALL")
				.setTotalAmount(totalAmount).setOutTradeNo(outTradeNo).setUndiscountableAmount(undiscountableAmount)
				.setSellerId("").setBody(body).setOperatorId(operatorId).setStoreId(storeId)
				.setExtendParams(extendParams).setTimeoutExpress(timeoutExpress)
				.setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))// 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
				.setGoodsDetailList(goodsDetailList);

		ServerResponse response = null;
		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
		switch (result.getTradeStatus()) {
		case SUCCESS:
			// log.info("支付宝预下单成功: )");

			// 生成二维码
			AlipayTradePrecreateResponse res = result.getResponse();
			File qrFile = new File(path);
			if (!qrFile.exists()) {
				qrFile.setWritable(true);
				qrFile.mkdirs();
			}
			String qrFileName = "qr_" + orderNo + ".png";
			String qrPath = path + "/" + qrFileName;
			ZxingUtil.getQRCodeImge(res.getQrCode(), 256, qrPath);
			// 生成二维码结束

			String qrFileUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + qrFileName;
			List<File> files = new ArrayList<>();
			File targetFile = new File(qrPath);
			files.add(targetFile);
			FtpUtil.uploadFile(files);
			targetFile.delete();

			Map data = new HashMap<>();
			data.put("orderNo", orderNo);
			data.put("qrPath", qrFileUrl);

			response = ServerResponse.createSuccess("获取成功", data);
			break;

		case FAILED:
			// log.error("支付宝预下单失败!!!");
			response = ServerResponse.createErrorMessage("支付宝预下单失败");
			break;

		case UNKNOWN:
			// log.error("系统异常，预下单状态未知!!!");
			response = ServerResponse.createErrorMessage("系统异常，预下单状态未知");
			break;

		default:
			// log.error("不支持的交易状态，交易返回异常!!!");
			response = ServerResponse.createErrorMessage("不支持的交易状态");
			break;
		}
		return response;

	}

	@Override
	public ServerResponse alipayCallback(Map paramMap) {
		String tradeStatus = (String) paramMap.get("trade_status");
		long orderNo = Long.valueOf((String) paramMap.get("out_trade_no"));
		String totalAmount = (String) paramMap.get("total_amount");
		String platformNumber = (String) paramMap.get("trade_no");

		Order order = orderMapper.selectByOrderNo(orderNo);
		if (order == null)
			// 非ppmall商城
			return ServerResponse.createErrorMessage("非PPMALL订单");

		if (!order.getPayment().toString().equals(totalAmount))
			return ServerResponse.createErrorMessage("订单金额错误");

		if (tradeStatus.equals(Const.AliPayStatus.TRADE_SUCCESS.getStatus())) {
			order.setPaymentType(Const.PayType.ALIPAY.getCode());
			order.setStatus(Const.OrderStatus.PAID.getCode());
			order.setPaymentTime(DateUtil.getDate());
			orderMapper.updateByOrderNoSelective(order);
		}

		PayInfo payInfo = new PayInfo();
		Date now = DateUtil.getDate();
		payInfo.setCreateTime(now);
		payInfo.setOrderNo(orderNo);
		payInfo.setPayPlatform(Const.PayType.ALIPAY.getCode());
		payInfo.setPlatformNumber(platformNumber);
		payInfo.setPlatformStatus(tradeStatus);
		payInfo.setUpdateTime(now);
		payInfo.setUserId(order.getUserId());
		payInfoMapper.insert(payInfo);

		return ServerResponse.createSuccess();
	}

	@Override
	public ServerResponse queryOrderPayStatus(Long orderNo) {
		Order order = orderMapper.selectByOrderNo(orderNo);
		if (order == null) {
			return ServerResponse.createErrorMessage("用户没有该订单");
		}
		if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) {
			return ServerResponse.createSuccess();
		}
		return ServerResponse.createError();
	}

}
