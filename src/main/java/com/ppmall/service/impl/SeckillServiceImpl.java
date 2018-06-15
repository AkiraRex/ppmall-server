package com.ppmall.service.impl;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.ServerResponse;
import com.ppmall.component.ThreadPoolManager;
import com.ppmall.pojo.Product;
import com.ppmall.service.ISeckillService;

@Service
public class SeckillServiceImpl implements ISeckillService {
	private static ConcurrentLinkedQueue<Product> queue = new ConcurrentLinkedQueue<>();// 队列

	int count = 30;

	@Autowired
	ThreadPoolManager tpm;

	@Override
	public ServerResponse createOrder(int productId) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 500; i++) {
			// 模拟并发500条记录
			tpm.processOrders(Integer.toString(i));
		}
		return ServerResponse.createSuccessMessage("ok");
	}

}
