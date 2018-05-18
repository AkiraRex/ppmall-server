package com.ppmall.service;

import java.util.List;

import com.ppmall.common.ServerResponse;

public interface ICartService {
	ServerResponse getCartCount(int userId);
	
	ServerResponse getCartList(int userId);
}
