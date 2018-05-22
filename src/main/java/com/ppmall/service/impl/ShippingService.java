package com.ppmall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.ShippingMapper;
import com.ppmall.pojo.Shipping;
import com.ppmall.service.IShippingService;
import com.ppmall.util.DateUtil;

@Service("iShippingService")
public class ShippingService implements IShippingService {

	@Autowired
	private ShippingMapper shippingMapper;

	@Override
	public ServerResponse getShippingList(int userId, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List shippingList = shippingMapper.selectByUserId(userId);
		PageInfo pageInfo = new PageInfo<>(shippingList);
		return ServerResponse.createSuccess("获取成功", pageInfo);
	}

	@Override
	public ServerResponse addShipping(int userId, Shipping shipping) {
		// TODO Auto-generated method stub
		Date date = DateUtil.getDate();
		shipping.setCreateTime(date);
		shipping.setUpdateTime(date);
		shippingMapper.insert(shipping);
		return ServerResponse.createSuccess("添加成功");
	}

}
