package com.ppmall.component;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ppmall.pojo.Product;
import com.ppmall.service.ICartService;
import com.ppmall.service.IOrderService;
import com.ppmall.service.impl.CartServiceImpl;
import com.ppmall.service.impl.OrderServiceImpl;

@Component
public class DealSeckillThread implements Runnable {

	private ConcurrentLinkedQueue<Product> queue;

	private Logger logger = LoggerFactory.getLogger(DealSeckillThread.class);

	//@Autowired
	private IOrderService iOrderService;

	//@Autowired
	private ICartService iCartService;

	public DealSeckillThread() {
		
	}

	public DealSeckillThread(ConcurrentLinkedQueue<Product> queue) {
		this.queue = queue;
		this.iCartService = new CartServiceImpl();
		this.iOrderService = new OrderServiceImpl();
	}

	@Override
	public void run() {
		try {
			// TODO Auto-generated method stub
			while (true) {
				System.out.println(queue.isEmpty());
				if (queue != null && !queue.isEmpty()) {
					int productId = queue.poll().getId();
					iCartService.addToCart(productId, 1, 22);
					iOrderService.createOrder(22, 4);
					
					logger.info("下单成功" + productId);
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
