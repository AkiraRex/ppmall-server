package com.ppmall.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.Const;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.UserMapper;
import com.ppmall.pojo.User;
import com.ppmall.service.ITokenService;
import com.ppmall.util.JsonUtil;
import com.ppmall.util.TokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;

@Service("iTokenService")
public class TokenServiceImpl implements ITokenService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse refreshToken(String refreshToken, User user) {
		// TODO Auto-generated method stub
		try {
			Map refreshTokenClaims = TokenUtil.parseToken(refreshToken);
			
			if (user == null)
				user = userMapper.selectByPrimaryKey((Integer) refreshTokenClaims.get("userId"));
			
			Map claims = new HashMap<>();
			claims.put("user", user);
			String accessToken = TokenUtil.createToken(claims, String.valueOf(user.getId()),
					Const.ExpiredType.ONE_HOUR * 2);
			
			return ServerResponse.createSuccess(accessToken);
		} catch (ExpiredJwtException e) {
			return ServerResponse.createErrorStatus(ResponseCode.TOKEN_EXPIRED.getCode(), "token已过期");
		} catch (InvalidClaimException e) {
			return ServerResponse.createErrorStatus(ResponseCode.TOKEN_INVALID.getCode(), "token不合法");
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createErrorStatus(ResponseCode.TOKEN_ERROR.getCode(), "token错误");
		}

	}

}
