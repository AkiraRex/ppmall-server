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
import com.ppmall.component.activity.ActivityThread;
import com.ppmall.component.activity.ActivityThreadService;
import com.ppmall.dao.ActivityMapper;
import com.ppmall.pojo.Activity;
import com.ppmall.pojo.Product;
import com.ppmall.rabbitmq.message.ActivityMessage;
import com.ppmall.rabbitmq.producer.ISecKillMessageProducer;
import com.ppmall.service.ActivityService;
import com.ppmall.util.DateUtil;

@Service
public class ActivityServiceImpl implements ActivityService, InitializingBean {
	private static ConcurrentLinkedQueue<Product> queue = new ConcurrentLinkedQueue<>();// 队列

	@Autowired
	ActivityThreadService tpm;

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private ActivityThread dealSeckillThread;

	@Autowired
	private ISecKillMessageProducer iSecKillMessageProducer;

	@Autowired
	RedisUtil redisUtil;

	@Override
	public synchronized ServerResponse createOrder(ActivityMessage message) {
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
