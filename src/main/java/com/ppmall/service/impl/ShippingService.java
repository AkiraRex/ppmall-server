package com.ppmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.ServerResponse;
import com.ppmall.dao.ShippingMapper;
import com.ppmall.service.IShippingService;

@Service("iShippingService")
public class ShippingService implements IShippingService {

	@Autowired
	private ShippingMapper shippingMapper;
	
	@Override
	public ServerResponse getShippingList(int userId) {
		// TODO Auto-generated method stub
		List shippingList = shippingMapper.selectByUserId(userId);
		return ServerResponse.createSuccess("获取成功", shippingList);
	}

}
