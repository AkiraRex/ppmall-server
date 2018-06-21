package com.ppmall.rabbitmq.producer.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ppmall.rabbitmq.producer.ISecKillMessageProducer;

@Component
public class SecKillMessageProducerImpl implements ISecKillMessageProducer {

	@Autowired
	AmqpTemplate amqpTemplate;

	@Override
	public void sendMessage(String exchange, String key, Object object) {
		// TODO Auto-generated method stub
		try {
			System.out.println("=========发送消息开始=============消息：" + object.toString());
			amqpTemplate.convertAndSend("testExchange", key, object);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
