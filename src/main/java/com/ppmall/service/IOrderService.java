package com.ppmall.service;

import com.ppmall.common.ServerResponse;

public interface IOrderService {
	ServerResponse getOrderList(Long orderNum, int pageNum, int pageSize);

	ServerResponse getOrderDetail(Long orderNo);

	ServerResponse getOrderCart(int userId);

	ServerResponse createOrder(int userId, int shippingId);
}
