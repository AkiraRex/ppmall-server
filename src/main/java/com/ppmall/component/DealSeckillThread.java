package com.ppmall.component;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;

import com.ppmall.pojo.Product;

@Component
public class DealSeckillThread implements Runnable {

	private ConcurrentLinkedQueue<Product> queue;

	public DealSeckillThread() {
		// TODO Auto-generated constructor stub
	}

	public DealSeckillThread(ConcurrentLinkedQueue<Product> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (queue != null && !queue.isEmpty()) {
			
            System.out.println("下单成功" + queue.poll().getId());
        }

	}

}
