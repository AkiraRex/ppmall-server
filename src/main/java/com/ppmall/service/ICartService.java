package com.ppmall.service;

import java.util.List;

import com.ppmall.common.ServerResponse;

public interface ICartService {
	ServerResponse getCartCount(int userId);

	ServerResponse getCartList(int userId);
	
	ServerResponse getCartListByChecked(int userId,int checked);

	ServerResponse addToCart(int productId, int count, int userId);

	ServerResponse deleteCart(int[] productIds, int userId);

	ServerResponse updateCart(Integer productId, int userId, Integer count, int checked);

}
