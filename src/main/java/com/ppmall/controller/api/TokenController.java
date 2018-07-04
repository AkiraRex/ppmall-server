package com.ppmall.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.ITokenService;

@Controller
@RequestMapping("/api/tokens")
public class TokenController {
	
	@Autowired
	private ITokenService iTokenServcie;
	
	@RequestMapping(value = "/refresh_token.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse refreshToken(String refreshToken ,HttpServletRequest request,HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		return iTokenServcie.refreshToken(refreshToken, user);
		
	}
	
}
