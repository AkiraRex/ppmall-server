package com.ppmall.service;

import java.util.Date;

import com.ppmall.common.ServerResponse;
import com.ppmall.rabbitmq.message.ActivityMessage;

public interface ActivityService {
	ServerResponse createOrder(ActivityMessage message);
	
	ServerResponse listAllKillActivities(Date beginTime, Date endTime);
}
