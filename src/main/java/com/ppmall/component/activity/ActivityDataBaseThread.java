package com.ppmall.component.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ppmall.rabbitmq.message.ActivityMessage;
import com.ppmall.service.ICartService;
import com.ppmall.service.IOrderService;

@Component("dBThread")
@Scope("prototype") // spring 多例
public class ActivityDataBaseThread implements Runnable {
	private ActivityMessage msg;
	private Logger logger = LoggerFactory.getLogger(ActivityDataBaseThread.class);

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

	public ActivityMessage getMsg() {
		return this.msg;
	}

	public void setMsg(ActivityMessage msg) {
		this.msg = msg;
	}
}