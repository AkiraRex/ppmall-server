package com.ppmall.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ppmall.service.ICartService;
import com.ppmall.service.IOrderService;

@Component("dBThread")
@Scope("prototype") // spring 多例
public class DBThread implements Runnable {
	private String msg;
	private Logger logger = LoggerFactory.getLogger(DBThread.class);

	@Autowired
	private IOrderService iOrderService;

	@Autowired
	private ICartService iCartService;

	@Override
	public void run() {
		// 模拟在数据库插入数据
		int productId = Integer.valueOf(msg);
		iCartService.addToCart(productId, 1, 22);
		iOrderService.createOrder(22, 4);
		
		logger.info("下单成功" + productId);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}