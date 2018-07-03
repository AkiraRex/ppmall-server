package com.ppmall.service;

import java.util.Map;

import com.ppmall.common.ServerResponse;

public interface ITokenService {
	
	ServerResponse createNewToken(Map claims,String subject,long ttlAtMillis);
	
}
