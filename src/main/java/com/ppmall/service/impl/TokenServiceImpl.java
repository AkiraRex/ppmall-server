package com.ppmall.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.ITokenService;
import com.ppmall.util.TokenUtil;

@Service("iTokenService")
public class TokenServiceImpl implements ITokenService{

	@Override
	public ServerResponse createNewToken(Map claims, String subject, long ttlAtMillis) {
		// TODO Auto-generated method stub
		String token = TokenUtil.createToken(claims, subject, ttlAtMillis);
		return ServerResponse.createSuccess("创建成功", token);
	}

}
