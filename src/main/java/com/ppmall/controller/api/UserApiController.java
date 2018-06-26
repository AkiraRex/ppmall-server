package com.ppmall.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.service.IUserService;

@Controller
@RequestMapping("/api/user")
public class UserApiController {
	@Autowired
	private IUserService iUserService;

	@RequestMapping(value = "/wechat_login.do", method = RequestMethod.POST)
	public ServerResponse wechatLogin(String code, String encryptedData, String iv, HttpSession session) {
		ServerResponse response = iUserService.wechatLogin(code, encryptedData, iv);
		if (response.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
}
