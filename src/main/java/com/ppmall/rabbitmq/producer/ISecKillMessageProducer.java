package com.ppmall.rabbitmq.producer;

public interface ISecKillMessageProducer {
	public void sendMessage(String exchange, String key, Object object);
}
