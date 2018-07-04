package com.ppmall.service;

import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;

public interface ITokenService {
	
	public ServerResponse refreshToken(String refreshToken, User user);
	
}
