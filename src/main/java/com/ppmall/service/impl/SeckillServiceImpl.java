package com.ppmall.service.impl;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.ppmall.common.ServerResponse;
import com.ppmall.component.DealSeckillThread;
import com.ppmall.pojo.Product;
import com.ppmall.service.ISeckillService;

public class SeckillServiceImpl implements ISeckillService {
	private static ConcurrentLinkedQueue<Product> queue = new ConcurrentLinkedQueue<>();// 队列

	int count = 30;
	
	@Autowired
	private DealSeckillThread thread;
	
	@Override
	public ServerResponse createOrder(int productId) {
		// TODO Auto-generated method stub
		if (count > 0) {
			Product product = new Product();
			product.setId(productId);
			queue.add(product);
			count--;
			return ServerResponse.createSuccessMessage("抢购成功");
		}
		return ServerResponse.createSuccessMessage("抢购失败,下次继续努力");
	}

}
