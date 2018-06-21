package com.ppmall.rabbitmq.listener;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.ppmall.service.ICartService;
import com.ppmall.service.IOrderService;



public class SecKillQueueListener implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(SecKillQueueListener.class);
	
	@Autowired
	private ICartService iCartService;
	
	@Autowired
	private IOrderService iOrderService;
	
	public SecKillQueueListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("=============监听【QueueListenter】消息" + message);
		try {
			int productId = Integer.valueOf(new String(message.getBody(),"UTF-8"));
			iCartService.addToCart(productId, 1, 22);
			iOrderService.createOrder(22, 4);
			
			logger.info("下单成功" + productId);
			System.out.print("=====获取消息" + new String(message.getBody(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
