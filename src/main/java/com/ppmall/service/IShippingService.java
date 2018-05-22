package com.ppmall.service;

import com.ppmall.common.ServerResponse;

public interface IShippingService {
	ServerResponse getShippingList(int userId);
}
