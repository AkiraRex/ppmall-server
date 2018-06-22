package com.ppmall.service;

import java.util.Date;

import com.ppmall.common.ServerResponse;
import com.ppmall.rabbitmq.message.SecKillMessage;

public interface ISecKillService {
	ServerResponse createOrder(SecKillMessage message);
	
	ServerResponse listAllKillActivities(Date beginTime, Date endTime);
}
