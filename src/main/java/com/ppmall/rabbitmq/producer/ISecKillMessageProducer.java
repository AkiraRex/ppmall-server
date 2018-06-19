package com.ppmall.rabbitmq.producer;

public interface ISecKillMessageProducer {
	public void sendMessage(String key, Object object);
}
