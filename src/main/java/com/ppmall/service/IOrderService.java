package com.ppmall.service;

import com.ppmall.common.ServerResponse;

public interface IOrderService {
	ServerResponse getOrderList(Long orderNum, int pageNum, int pageSize);

	ServerResponse getOrderDetail(Long orderNo);

	ServerResponse getOrderCart(int userId);

	ServerResponse createOrder(int userId, int shippingId);
	
	ServerResponse getOrderList(int userId, int pageNum, int pageSize);
	
	ServerResponse cancelOrder(Long orderNo);
	
	ServerResponse payForOrder(Long orderNo);
}
