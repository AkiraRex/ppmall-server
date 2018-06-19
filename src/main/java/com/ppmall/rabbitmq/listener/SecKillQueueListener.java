package com.ppmall.rabbitmq.listener;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SecKillQueueListener implements MessageListener {

	public SecKillQueueListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("=============监听【QueueListenter】消息" + message);
		try {
			System.out.print("=====获取消息" + new String(message.getBody(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
