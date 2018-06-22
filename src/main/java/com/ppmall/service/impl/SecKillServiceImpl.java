package com.ppmall.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ppmall.redis.RedisUtil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.ServerResponse;
import com.ppmall.component.seckill.DealSeckillThread;
import com.ppmall.component.seckill.SecKillThreadService;
import com.ppmall.dao.ActivityMapper;
import com.ppmall.pojo.Activity;
import com.ppmall.pojo.Product;
import com.ppmall.rabbitmq.message.SecKillMessage;
import com.ppmall.rabbitmq.producer.ISecKillMessageProducer;
import com.ppmall.service.ISecKillService;
import com.ppmall.util.DateUtil;

@Service
public class SecKillServiceImpl implements ISecKillService, InitializingBean {
	private static ConcurrentLinkedQueue<Product> queue = new ConcurrentLinkedQueue<>();// 队列

	@Autowired
	SecKillThreadService tpm;

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private DealSeckillThread dealSeckillThread;

	@Autowired
	private ISecKillMessageProducer iSecKillMessageProducer;

	@Autowired
	RedisUtil redisUtil;

	@Override
	public synchronized ServerResponse createOrder(SecKillMessage message) {
		// TODO Auto-generated method stub
		int count = Integer.valueOf(redisUtil.get("count").toString());
		if (count > 0) {
			// 线程池处理方式(线程池原理类似于消息队列,自带一个队列)

			tpm.processOrders(message);

			// 普通多线程方式处理队列
			Product product = new Product();
			product.setId(count);
			queue.offer(product);

			// rabbitmq消息队列(生产者),消费者在com.ppmall.rabbitmq.listener.SecKillQueueListener.onMessage()
			// iSecKillMessageProducer.sendMessage("testExchange",
			// "SecKillQueue", productId);
			redisUtil.decr("count", 1);
			return ServerResponse.createSuccessMessage("抢购成功");
		}
		return ServerResponse.createSuccessMessage("抢购失败");
	}

	@Override
	public ServerResponse listAllKillActivities(Date beginTime, Date endTime) {
		// TODO Auto-generated method stub
		if (beginTime == null) {
			beginTime = DateUtil.getDate();
		}

		if (endTime == null) {
			long weekMilis = 1000 * 60 * 60 * 24 * 7; // 7日内抢购活动
			endTime = DateUtil.getDate(beginTime.getTime() + weekMilis);
		}

		Map paramMap = new HashMap<>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		List<Activity> killList = activityMapper.selectByActivityDuration(paramMap);
		return ServerResponse.createSuccess("获取成功", killList);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		// new Thread(new DealSeckillThread(queue)).start();

	}

}
