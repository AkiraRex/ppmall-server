package com.ppmall.component.seckill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ppmall.rabbitmq.message.SecKillMessage;
import com.ppmall.service.ICartService;
import com.ppmall.service.IOrderService;

@Component("dBThread")
@Scope("prototype") // spring 多例
public class SecKillDataBaseThread implements Runnable {
	private SecKillMessage msg;
	private Logger logger = LoggerFactory.getLogger(SecKillDataBaseThread.class);

	@Autowired
	private IOrderService iOrderService;

	@Autowired
	private ICartService iCartService;

	@Override
	public void run() {
		// 模拟在数据库插入数据
		// int userId = (int) (Math.random() * (100));
		int userId = getMsg().getUserId();
		int productId = getMsg().getProductId();
		int quantity = getMsg().getQuantity();
		int shippingId = getMsg().getShippingId();
		iCartService.addToCart(productId, quantity, userId);
		iOrderService.createOrder(userId, shippingId);

		logger.info("下单成功" + productId);
	}

	public SecKillMessage getMsg() {
		return this.msg;
	}

	public void setMsg(SecKillMessage msg) {
		this.msg = msg;
	}
}