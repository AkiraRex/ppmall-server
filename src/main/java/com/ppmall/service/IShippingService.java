package com.ppmall.service;

import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.Shipping;

public interface IShippingService {
	ServerResponse getShippingList(int userId, int pageNum, int pageSize);

	ServerResponse addShipping(int userId, Shipping shipping);
	
	ServerResponse selectShipping(int shiippingId);
	
	ServerResponse saveShipping(Shipping shipping);
	
	ServerResponse deleteShipping(int shipping);
}
