package com.ppmall.service;

import java.util.Date;

import com.ppmall.common.ServerResponse;

public interface ISecKillService {
	ServerResponse createOrder(int productId);
	
	ServerResponse listAllKillActivities(Date beginTime, Date endTime);
}
